package com.obss.metro.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.obss.metro.v1.dto.candidate.CandidateRequestDTO;
import com.obss.metro.v1.dto.candidate.CandidateResponseDTO;
import com.obss.metro.v1.service.CandidateService;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CandidateController {
  private final CandidateService candidateService;
  
  @PostMapping(consumes = "application/json")
  public ResponseEntity<String> postCandidate(
          @NotNull @RequestBody final CandidateRequestDTO userDTO, @NotNull final Principal principal) {
    final CandidateResponseDTO user = candidateService.saveCandidate(userDTO, principal.getName());
    final URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/%s".formatted(user.id()))
            .build()
            .toUri();

    return ResponseEntity.created(location).body("ok");
  }

  @GetMapping("/{id}")
  public CandidateResponseDTO getCandidateById(@PathVariable UUID id) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return candidateService.findCandidateById(id, auth);
  }
  
  @PostMapping(value = "/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public void postCandidateURLById(@PathVariable final String id, @NotNull @RequestParam final Map<String, String> body) throws JsonProcessingException {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    candidateService.fetchCandidateProfile(id, body.get("url"), auth);
  }
}
