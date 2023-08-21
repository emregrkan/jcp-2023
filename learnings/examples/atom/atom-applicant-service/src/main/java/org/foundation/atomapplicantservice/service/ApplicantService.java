package org.foundation.atomapplicantservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomapplicantservice.dto.ApplicantRequestDTO;
import org.foundation.atomapplicantservice.dto.ApplicantResponseDTO;
import org.foundation.atomapplicantservice.entity.Applicant;
import org.foundation.atomapplicantservice.repository.ApplicantRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        final ApplicantResponseDTO responseDTO = modelMapper.map(applicantRepository.save(applicant), ApplicantResponseDTO.class);
        final String data = objectMapper.writeValueAsString(responseDTO);
        log.info("Applicant Response DTO: {}", data);
        kafkaTemplate.send("profiles.changed", data).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message Sent");
            } else {
                log.warn("Message could not sent");
            }
        });
        return responseDTO;
    }

    public ApplicantResponseDTO findApplicantByID(final UUID id) {
        final Applicant applicant = applicantRepository.findById(id).orElseThrow(RuntimeException::new);
        return modelMapper.map(applicant, ApplicantResponseDTO.class);
    }
}
