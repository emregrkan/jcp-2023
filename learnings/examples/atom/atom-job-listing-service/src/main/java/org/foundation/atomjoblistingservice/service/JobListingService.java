package org.foundation.atomjoblistingservice.service;

import lombok.RequiredArgsConstructor;
import org.foundation.atomjoblistingservice.dto.*;
import org.foundation.atomjoblistingservice.entity.JobListing;
import org.foundation.atomjoblistingservice.entity.attributes.Application;
import org.foundation.atomjoblistingservice.repository.JobListingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobListingService {
    private final JobListingRepository jobListingRepository;
    private final ModelMapper modelMapper;

    public JobListingResponseDTO createJobListing(final JobListingRequestDTO jobListingRequestDTO) {
        final JobListing jobListing = modelMapper.map(jobListingRequestDTO, JobListing.class);
        return modelMapper.map(jobListingRepository.save(jobListing), JobListingResponseDTO.class);
    }

    public Page<JobListingFlatResponseDTO> findAllJobListings(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        return jobListingRepository.findAll(pageRequest).map(jobListing -> modelMapper.map(jobListing, JobListingFlatResponseDTO.class));
    }

    public JobListingResponseDTO findJobListingById(final String id) {
        final JobListing jobListing = jobListingRepository.findById(id).orElseThrow(RuntimeException::new);
        return modelMapper.map(jobListing, JobListingResponseDTO.class);
    }

    public Page<Application> findApplicationsByJobId(final String id, final int page, final int size) {

        final Pageable pageRequest = PageRequest.of(page, size);
        final Set<Application> applications = jobListingRepository.findById(id).orElseThrow(RuntimeException::new).getApplications();

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), applications.size());

        final List<Application> pageContent = applications.stream().toList().subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, applications.size());
    }

    public void addJobApplication(final ApplicationResponseDTO applicationResponseDTO) {
        final JobListing jobListing = jobListingRepository.findById(applicationResponseDTO.getJob().getId()).orElseThrow(RuntimeException::new);
        final Application application = modelMapper.map(applicationResponseDTO, Application.class);
        final Set<Application> applications = jobListing.getApplications();

        applications.add(application);
        jobListing.setApplications(applications);

        jobListingRepository.save(jobListing);
    }
}
