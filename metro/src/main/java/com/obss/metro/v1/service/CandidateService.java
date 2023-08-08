package com.obss.metro.v1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.metro.v1.dto.candidate.*;
import com.obss.metro.v1.dto.jobapplication.JobApplicationResponseDTO;
import com.obss.metro.v1.entity.Candidate;
import com.obss.metro.v1.entity.CandidateEducation;
import com.obss.metro.v1.entity.CandidateExperience;
import com.obss.metro.v1.exception.impl.ResourceNotFoundException;
import com.obss.metro.v1.repository.CandidateRepository;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CandidateService {
  private final CandidateRepository candidateRepository;
  private final RabbitTemplate rabbitTemplate;
  private final ObjectMapper objectMapper;

  @RabbitListener(queues = "q.candidate-profile")
  public void saveFetchedProfile(byte[] responseDto) {
    try {
      final ProfileResponseDTO profile =
          objectMapper.readValue(responseDto, ProfileResponseDTO.class);

      final Candidate candidate =
          candidateRepository
              .findById(UUID.fromString(profile.id()))
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException("id", "Candidate with given id is not found"));

      candidate.setHeadline(profile.headline());
      candidate.setLocation(profile.location());
      candidate.setAbout(profile.about());

      final var experience = candidate.getExperience();
      experience.clear();
      experience.addAll(
          profile.experience().stream()
              .map(
                  exp ->
                      new CandidateExperience(
                          null,
                          candidate,
                          exp.title(),
                          exp.companyName(),
                          exp.companyPage(),
                          exp.location(),
                          exp.duration()))
              .toList());

      final var education = candidate.getEducation();
      education.clear();
      education.addAll(
          profile.education().stream()
              .map(
                  edu ->
                      new CandidateEducation(
                          null, candidate, edu.school(), edu.field(), edu.degree(), edu.duration()))
              .toList());

      candidate.setExperience(experience);
      candidate.setEducation(education);
      candidate.setInUrl(profile.url());

      candidateRepository.save(candidate);
    } catch (Exception ignore) {
      try {
        final ProfileRequestDTO failedRequestDTO =
            objectMapper.readValue(responseDto, ProfileRequestDTO.class);
        log.error("Could not fetch the requested profile. ID: %s".formatted(failedRequestDTO.id()));
      } catch (Exception ex) {
        log.error("Request failed on crawler");
      }
    }
  }

  public void saveCandidate(@NotNull final CandidateAuthRequestDTO dto, @NotNull final String id) {
    final Candidate user = dto.toCandidate(UUID.fromString(id));
    candidateRepository.save(user);
  }

  public void fetchCandidateProfile(@NotNull final String id, @NotNull final String url)
      throws JsonProcessingException {
    if (!candidateRepository.existsById(UUID.fromString(id))) {
      throw new ResourceNotFoundException("id", "Resource with requested id not found");
    }

    final ProfileRequestDTO requestDTO = new ProfileRequestDTO(id, url);
    rabbitTemplate.convertAndSend("q.candidate-url", objectMapper.writeValueAsBytes(requestDTO));
  }

  public CandidateAuthResponseDTO findCurrentCandidate(final String id) {
    final Candidate candidate =
        candidateRepository
            .findById(UUID.fromString(id))
            .orElseThrow(
                () -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    return new CandidateAuthResponseDTO(
        candidate.getId(),
        candidate.getFirstName(),
        candidate.getLastName(),
        candidate.getEmail(),
        candidate.getProfilePicture(),
        candidate.getInUrl());
  }

  public Callable<ProfileResponseDTO> findCurrentCandidateProfile(final String id) {
    return () -> {
      try {
        while (true) {
          final Candidate candidate =
              candidateRepository
                  .findById(UUID.fromString(id))
                  .orElseThrow(
                      () ->
                          new ResourceNotFoundException(
                              "id", "Resource with requested id not found"));
          if (candidate.getHeadline() != null) return ProfileResponseDTO.fromCandidate(candidate);
          Thread.sleep(420);
        }
      } catch (InterruptedException ex) {
        log.error("Interrupted: %s".formatted(ex.getMessage()));
        return null;
      }
    };
  }

  public Set<JobApplicationResponseDTO> findCurrentCandidateApplications(final String id) {
    final Candidate candidate =
        candidateRepository
            .findById(UUID.fromString(id))
            .orElseThrow(
                () -> new ResourceNotFoundException("id", "Resource with requested id not found"));

    return candidate.getApplications().stream()
        .map(JobApplicationResponseDTO::fromJobApplication)
        .collect(Collectors.toSet());
  }

  public Page<CandidateFullResponseDTO> findAllCandidates(int page, int candidates) {
    final PageRequest request = PageRequest.of(page, candidates);
    return candidateRepository.findAll(request).map(CandidateFullResponseDTO::fromCandidate);
  }

  public CandidateFullResponseDTO findCandidateById(final UUID id) {
    final Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    return CandidateFullResponseDTO.fromCandidate(candidate);
  }
}
