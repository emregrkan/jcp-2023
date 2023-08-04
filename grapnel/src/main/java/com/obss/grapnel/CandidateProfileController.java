package com.obss.grapnel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.grapnel.dto.ProfileRequestDTO;
import com.obss.grapnel.dto.ProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CandidateProfileController {
  private final CandidateProfileService service;
  private final ObjectMapper objectMapper;

  @RabbitListener(queues = "q.candidate-url")
  @SendTo("q.candidate-profile")
  private byte[] getCandidateProfile(byte[] dto) {
    try {
      final ProfileRequestDTO requestDTO = objectMapper.readValue(dto, ProfileRequestDTO.class);
      final ProfileResponseDTO responseDTO = service.fetchCandidateProfile(requestDTO);
      log.info("Request: {id: %s, url: %s}".formatted(requestDTO.id(), requestDTO.url()));
      return objectMapper.writeValueAsBytes(responseDTO);
    } catch (Exception ignore) {
      return "{error:\"Could not fetch the requested profile\"}".getBytes();
    }
  }
}
