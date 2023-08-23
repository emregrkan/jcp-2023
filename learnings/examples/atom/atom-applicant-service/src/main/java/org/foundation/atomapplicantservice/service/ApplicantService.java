package org.foundation.atomapplicantservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomapplicantservice.dto.ApplicantRequestDTO;
import org.foundation.atomapplicantservice.dto.ApplicantResponseDTO;
import org.foundation.atomapplicantservice.dto.ApplicationResponseDTO;
import org.foundation.atomapplicantservice.entity.Applicant;
import org.foundation.atomapplicantservice.entity.attributes.Application;
import org.foundation.atomapplicantservice.repository.ApplicantRepository;
import org.modelmapper.ModelMapper;
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
import java.util.concurrent.CompletableFuture;

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
        final ApplicantResponseDTO responseDTO = modelMapper.map(applicantRepository.save(applicant), ApplicantResponseDTO.class);
        final String data = objectMapper.writeValueAsString(responseDTO);

        log.info("Sending DTO to topic \"applicants.created\": {}", data);

        kafkaTemplate.send("applicants.created", data).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message Sent");
            } else {
                log.warn("Message could not sent");
            }
        });

        return responseDTO;
    }

    public ApplicantResponseDTO findApplicantById(final UUID id) {
        final Applicant applicant = applicantRepository.findById(id).orElseThrow(RuntimeException::new);
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
        final Set<Application> applications = applicantRepository.findById(id).orElseThrow(RuntimeException::new).getApplications();

        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), applications.size());

        final List<Application> pageContent = applications.stream().toList().subList(start, end);
        return new PageImpl<>(pageContent, pageRequest, applications.size());
    }

    public void addApplication(final ApplicationResponseDTO applicationResponseDTO) {
        final UUID applicantId = applicationResponseDTO.getApplicant().getId();
        final Applicant applicant = applicantRepository.findById(applicantId).orElseThrow(RuntimeException::new);
        final Set<Application> applications = applicant.getApplications();
        final Application application = modelMapper.map(applicationResponseDTO, Application.class);

        applications.add(application);
        applicant.setApplications(applications);

        applicantRepository.save(applicant);
    }
}
