package org.foundation.atomapplicantservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomapplicantservice.dto.*;
import org.foundation.atomapplicantservice.entity.Applicant;
import org.foundation.atomapplicantservice.entity.attributes.Application;
import org.foundation.atomapplicantservice.repository.ApplicantRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ApplicantService {
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ApplicantRepository applicantRepository;

    public ApplicantResponseDTO createApplicant(final ApplicantRequestDTO applicantRequestDTO) throws JsonProcessingException {
        final Applicant applicant = modelMapper.map(applicantRequestDTO, Applicant.class);
        return sendEventReturnResponse(applicant, "applicants.created");
    }

    public ApplicantResponseDTO findApplicantById(final UUID id) {
        final Applicant applicant = applicantRepository.findById(id).orElseThrow(BadRequestException::new);
        return modelMapper.map(applicant, ApplicantResponseDTO.class);
    }

    public Page<ApplicantResponseDTO> findAllApplicants(final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        return applicantRepository
                .findAll(pageRequest)
                .map(applicant -> modelMapper.map(applicant, ApplicantResponseDTO.class));
    }

    public Page<Application> findApplicationsById(final UUID id, final int page, final int size) {
        final Pageable pageRequest = PageRequest.of(page, size);
        final Set<Application> applications = applicantRepository.findById(id).orElseThrow(BadRequestException::new).getApplications();

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), applications.size());

        final List<Application> pageContent = applications.stream().toList().subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, applications.size());
    }

    public void handleApplicationCreated(final ApplicationResponseDTO applicationResponseDTO) {
        final UUID applicantId = applicationResponseDTO.getApplicant().getId();
        final Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(BadRequestException::new);
        final Set<Application> applications = applicant.getApplications();
        final Application application = modelMapper.map(applicationResponseDTO, Application.class);

        applications.add(application);
        applicant.setApplications(applications);

        applicantRepository.save(applicant);
    }

    public void handleJobListingUpdated(final JobListingResponseDTO jobListingResponseDTO) {
        final Set<Applicant> applicants = applicantRepository.findApplicantsByAppliedJobListingId(jobListingResponseDTO.getId());
        applicants.forEach(
            applicant -> applicant.getApplications()
                .forEach(application -> application.setJob(jobListingResponseDTO))
        );
        applicantRepository.saveAll(applicants);
    }

    public ApplicantResponseDTO updateApplicantById(final UUID id, final ApplicantRequestDTO applicantRequestDTO) throws JsonProcessingException {
        if (id != applicantRequestDTO.getId()) throw new BadRequestException();
        final Applicant source = modelMapper.map(applicantRequestDTO, Applicant.class);
        final Applicant target = applicantRepository.findById(id).orElseThrow(BadRequestException::new);
        modelMapper.map(source, target);

        return sendEventReturnResponse(applicantRepository.save(target), "applicants.updated");
    }

    private ApplicantResponseDTO sendEventReturnResponse(Applicant applicant, String topic) throws JsonProcessingException {
        final ApplicantResponseDTO applicantResponseDTO = modelMapper.map(applicant, ApplicantResponseDTO.class);
        final String data = objectMapper.writeValueAsString(applicantResponseDTO);

        kafkaTemplate.send(topic, data).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message Sent");
            } else {
                log.warn("Message could not sent");
            }
        });

        return applicantResponseDTO;
    }
}
