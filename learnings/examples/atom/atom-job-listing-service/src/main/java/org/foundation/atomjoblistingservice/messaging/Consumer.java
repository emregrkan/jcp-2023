package org.foundation.atomjoblistingservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomjoblistingservice.dto.ApplicantResponseDTO;
import org.foundation.atomjoblistingservice.dto.ApplicationResponseDTO;
import org.foundation.atomjoblistingservice.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class Consumer {

    private final ObjectMapper objectMapper;
    private final JobListingService jobListingService;

    @KafkaListener(topics = "job-listings.created", groupId = "consumer.job-listing-service")
    public void listenJobListingEvents(String message) {
        log.info("Message received: {}", message);
    }

    @KafkaListener(topics = "applicants.updated", groupId = "consumer.job-listing-service")
    public void listenApplicantEvents(String message) throws JsonProcessingException {
        log.info("Message received: {}", message);
        final ApplicantResponseDTO applicantResponseDTO = objectMapper.readValue(message, ApplicantResponseDTO.class);
        jobListingService.handleApplicantUpdate(applicantResponseDTO);
    }

    @KafkaListener(topics = "applications.created", groupId = "consumer.job-listing-service")
    public void listenApplicationCreatedEvents(String message) throws JsonProcessingException {
        log.info("Message received: {}", message);
        final ApplicationResponseDTO applicationResponseDTO = objectMapper.readValue(message, ApplicationResponseDTO.class);
        jobListingService.handleJobApplicationCreated(applicationResponseDTO);
    }
}
