package org.foundation.atomapplicationservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomapplicationservice.dto.ApplicantDTO;
import org.foundation.atomapplicationservice.dto.ApplicationRequestDTO;
import org.foundation.atomapplicationservice.dto.ApplicationResponseDTO;
import org.foundation.atomapplicationservice.dto.JobListingDTO;
import org.foundation.atomapplicationservice.entity.Application;
import org.foundation.atomapplicationservice.entity.attributes.ApplicationStatus;
import org.foundation.atomapplicationservice.repository.ApplicationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    public ApplicationResponseDTO createApplication(final ApplicationRequestDTO applicationRequestDTO) throws JsonProcessingException {
        final Application application = modelMapper.map(applicationRequestDTO, Application.class);
        final ApplicationResponseDTO applicationResponseDTO = modelMapper.map(applicationRepository.save(application), ApplicationResponseDTO.class);
        final String data = objectMapper.writeValueAsString(applicationResponseDTO);

        kafkaTemplate.send("applications.created", data).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message Sent");
            } else {
                log.warn("Message could not sent");
            }
        });

        return applicationResponseDTO;
    }

    public void handleApplicantUpdated(final ApplicantDTO applicantDTO) {
        final Set<Application> applications = applicationRepository.findApplicationsByApplicant_Id(applicantDTO.getId());
        applications.forEach(application -> {
            application.setApplicant(applicantDTO);

            if (applicantDTO.getBlacklistReason() != null)
                application.setStatus(ApplicationStatus.REJECTED);
        });
    }

    public void handleJobListingUpdated(final JobListingDTO jobListingDTO) {
        final Set<Application> applications = applicationRepository.findApplicationsByJob_Id(jobListingDTO.getId());
        applications.forEach(application -> {
            application.setJob(jobListingDTO);
        });
        applicationRepository.saveAll(applications);
    }
}
