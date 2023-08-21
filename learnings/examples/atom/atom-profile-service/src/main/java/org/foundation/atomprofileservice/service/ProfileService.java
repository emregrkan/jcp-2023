package org.foundation.atomprofileservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.foundation.atomprofileservice.dto.ProfileRequestDTO;
import org.foundation.atomprofileservice.dto.ProfileResponseDTO;
import org.foundation.atomprofileservice.entity.Profile;
import org.foundation.atomprofileservice.repository.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ProfileService {
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ProfileRepository profileRepository;

    public ProfileResponseDTO createProfile(final ProfileRequestDTO profileRequestDTO) throws JsonProcessingException {
        final Profile profile = modelMapper.map(profileRequestDTO, Profile.class);
        final ProfileResponseDTO responseDTO = modelMapper.map(profileRepository.save(profile), ProfileResponseDTO.class);
        final String data = objectMapper.writeValueAsString(responseDTO);
        log.info("Profile Response DTO: {}", data);
        kafkaTemplate.send("profiles.changed", data).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Message Sent");
            } else {
                log.warn("Message could not sent");
            }
        });
        return responseDTO;
    }
}
