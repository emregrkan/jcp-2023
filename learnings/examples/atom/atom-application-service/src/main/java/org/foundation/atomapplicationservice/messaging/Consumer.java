package org.foundation.atomapplicationservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomapplicationservice.dto.ApplicantDTO;
import org.foundation.atomapplicationservice.dto.JobListingDTO;
import org.foundation.atomapplicationservice.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class Consumer {
    private final ObjectMapper objectMapper;
    private final ApplicationService applicationService;

    @KafkaListener(topics = "applicants.updated", groupId = "consumer.application-service")
    public void listenApplicationEvents(String message) throws JsonProcessingException {
        log.info("Message received: {}", message);
        final ApplicantDTO applicantDTO = objectMapper.readValue(message, ApplicantDTO.class);
        applicationService.handleApplicantUpdated(applicantDTO);
    }

    @KafkaListener(topics = "job-listings.updated", groupId = "consumer.application-service")
    public void listenJobListingEvents(String message) throws JsonProcessingException {
        log.info("Message received: {}", message);
        final JobListingDTO jobListingDTO = objectMapper.readValue(message, JobListingDTO.class);
        applicationService.handleJobListingUpdated(jobListingDTO);
    }
}
