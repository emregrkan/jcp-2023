package org.foundation.atomapplicantservice.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomapplicantservice.dto.ApplicationResponseDTO;
import org.foundation.atomapplicantservice.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class Consumer {
    private final ObjectMapper objectMapper;
    private final ApplicantService applicantService;

    @KafkaListener(topics = "applications.created", groupId = "consumer.applicant-service")
    public void listenApplicationEvents(String message) throws JsonProcessingException {
        log.info("Message received: {}", message);
        final ApplicationResponseDTO applicationResponseDTO = objectMapper.readValue(message, ApplicationResponseDTO.class);
        applicantService.addApplication(applicationResponseDTO);
    }
}
