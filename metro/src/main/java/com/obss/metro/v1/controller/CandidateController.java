package com.obss.metro.v1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.obss.metro.v1.dto.candidate.CandidateAuthRequestDTO;
import com.obss.metro.v1.dto.candidate.CandidateAuthResponseDTO;
import com.obss.metro.v1.dto.candidate.CandidateFullResponseDTO;
import com.obss.metro.v1.dto.candidate.ProfileResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationResponseDTO;
import com.obss.metro.v1.entity.Candidate;
import com.obss.metro.v1.service.CandidateService;
import com.obss.metro.v1.service.SearchService;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.*;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/candidates")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CandidateController {
  private final CandidateService candidateService;
  private final SearchService searchService;

  @GetMapping("/search")
  public Set<CandidateAuthResponseDTO> searchCandidates(
      @RequestParam Optional<String> q) {
    return searchService.findCandidates(q);
  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<Object> postCandidate(
      @NotNull @RequestBody final CandidateAuthRequestDTO dto, @NotNull final Principal principal) {
    candidateService.saveCandidate(dto, principal.getName());

    final URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/%s".formatted(principal.getName()))
            .build()
            .toUri();

    return ResponseEntity.created(location).body(Optional.empty());
  }

  @PostMapping(value = "/me/url", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public void postCandidateURLById(
      @NotNull @RequestParam final Map<String, String> body, @NotNull final Principal principal)
      throws JsonProcessingException {
    candidateService.fetchCandidateProfile(principal.getName(), body.get("url"));
  }

  @GetMapping("/me")
  public CandidateAuthResponseDTO getCurrentCandidate(@NotNull final Principal principal) {
    return candidateService.findCurrentCandidate(principal.getName());
  }

  @GetMapping("/me/profile")
  public Callable<ProfileResponseDTO> getCurrentCandidateProfile(
      @NotNull final Principal principal) {
    return candidateService.findCurrentCandidateProfile(principal.getName());
  }

  @GetMapping("/me/applications")
  public Set<JobApplicationResponseDTO> getCurrentCandidateApplications(
      @NotNull final Principal principal) {
    return candidateService.findCurrentCandidateApplications(principal.getName());
  }

  @GetMapping
  @PreAuthorize("hasRole('OPERATOR')")
  public Page<CandidateFullResponseDTO> getAllCandidatesPaged(
      @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> candidates) {
    int pageNumber = page.orElse(0);
    int candidatesSize = candidates.orElse(9);

    return candidateService.findAllCandidates(pageNumber, candidatesSize);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('OPERATOR')")
  public CandidateFullResponseDTO getCandidateById(@PathVariable UUID id) {
    return candidateService.findCandidateById(id);
  }
}
