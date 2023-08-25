package org.foundation.atomjoblistingservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomjoblistingservice.dto.*;
import org.foundation.atomjoblistingservice.entity.JobListing;
import org.foundation.atomjoblistingservice.entity.attributes.Application;
import org.foundation.atomjoblistingservice.repository.JobListingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JobListingService {
    private final JobListingRepository jobListingRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public JobListingResponseDTO createJobListing(final JobListingRequestDTO jobListingRequestDTO) {
        final JobListing jobListing = modelMapper.map(jobListingRequestDTO, JobListing.class);
        return modelMapper.map(jobListingRepository.save(jobListing), JobListingResponseDTO.class);
    }

    public Page<JobListingFlatResponseDTO> findAllJobListings(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        return jobListingRepository.findAll(pageRequest).map(jobListing -> modelMapper.map(jobListing, JobListingFlatResponseDTO.class));
    }

    public JobListingResponseDTO findJobListingById(final String id) {
        final JobListing jobListing = jobListingRepository.findById(id).orElseThrow(BadRequestException::new);
        return modelMapper.map(jobListing, JobListingResponseDTO.class);
    }

    public Page<Application> findApplicationsByJobId(final String id, final int page, final int size) {

        final Pageable pageRequest = PageRequest.of(page, size);
        final Set<Application> applications = jobListingRepository.findById(id).orElseThrow(BadRequestException::new).getApplications();

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), applications.size());

        final List<Application> pageContent = applications.stream().toList().subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, applications.size());
    }

    public void handleJobApplicationCreated(final ApplicationResponseDTO applicationResponseDTO) {
        final JobListing jobListing = jobListingRepository.findById(applicationResponseDTO.getJob().getId()).orElseThrow(BadRequestException::new);
        final Application application = modelMapper.map(applicationResponseDTO, Application.class);
        final Set<Application> applications = jobListing.getApplications();

        applications.add(application);
        jobListing.setApplications(applications);

        jobListingRepository.save(jobListing);
    }

    // highly ineffective
    public void handleApplicantUpdate(final ApplicantResponseDTO applicantResponseDTO) {
        final Set<JobListing> jobListings = jobListingRepository.findJobListingByApplicantId(applicantResponseDTO.getId());
        jobListings.forEach(jobListing -> jobListing.setApplications(
            jobListing.getApplications().stream().map(application -> {
                application.setApplicant(applicantResponseDTO);
                if (applicantResponseDTO.getBlacklistReason() != null)
                    application.setStatus("REJECTED");
                return application;
            }).collect(Collectors.toSet())
        ));
        jobListingRepository.saveAll(jobListings);
    }

    public JobListingResponseDTO updateJobListingById(final String id, final JobListingRequestDTO jobListingRequestDTO) throws JsonProcessingException {
        final JobListing target = jobListingRepository.findById(id).orElseThrow(BadRequestException::new);
        final JobListing source = modelMapper.map(jobListingRequestDTO, JobListing.class);
        modelMapper.map(source, target);
        final JobListingResponseDTO jobListingResponseDTO = modelMapper.map(jobListingRepository.save(target), JobListingResponseDTO.class);
        final String data = objectMapper.writeValueAsString(jobListingResponseDTO);
        kafkaTemplate.send("job-listings.updated", data).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message Sent");
            } else {
                log.warn("Message could not sent");
            }
        });
        return jobListingResponseDTO;
    }
}
