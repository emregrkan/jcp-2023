package com.obss.metro.v1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.metro.v1.dto.candidate.CandidateRequestDTO;
import com.obss.metro.v1.dto.candidate.CandidateResponseDTO;
import com.obss.metro.v1.dto.candidate.ProfileRequestDTO;
import com.obss.metro.v1.dto.candidate.ProfileResponseDTO;
import com.obss.metro.v1.entity.Candidate;
import com.obss.metro.v1.entity.CandidateEducation;
import com.obss.metro.v1.entity.CandidateExperience;
import com.obss.metro.v1.exception.impl.ForbiddenException;
import com.obss.metro.v1.exception.impl.ResourceNotFoundException;
import com.obss.metro.v1.exception.impl.UnauthorizedException;
import com.obss.metro.v1.repository.CandidateRepository;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
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
      log.error("Could not fetch the requested profile");
    }
  }

  public CandidateResponseDTO saveCandidate(
      @NotNull final CandidateRequestDTO userDTO, @NotNull final String id) {
    final Candidate user = userDTO.toCandidate(UUID.fromString(id));
    return CandidateResponseDTO.fromCandidate(candidateRepository.save(user));
  }

  public void fetchCandidateProfile(
      @NotNull final String id, @NotNull final String url, @NotNull final Authentication auth)
      throws JsonProcessingException {
    final ProfileRequestDTO requestDTO = new ProfileRequestDTO(id, url);
    final Jwt token = (Jwt) auth.getPrincipal();

    if (!token.getSubject().equals(id)) throw new UnauthorizedException();

    rabbitTemplate.convertAndSend("q.candidate-url", objectMapper.writeValueAsBytes(requestDTO));
  }

  public CandidateResponseDTO findCandidateById(
      @NotNull final UUID id, @NotNull final Authentication auth) {
    final Optional<Candidate> user = candidateRepository.findById(id);

    if (user.isEmpty()) {
      throw new ResourceNotFoundException("id", "Candidate with given id not found");
    }

    final String sub = ((Jwt) auth.getPrincipal()).getSubject();
    if (!Objects.equals(user.get().getId().toString(), sub)
        || auth.getAuthorities().stream()
            .anyMatch(role -> role.getAuthority().equals("ROLE_OPERATOR"))) {
      throw new ForbiddenException();
    }

    return CandidateResponseDTO.fromCandidate(user.get());
  }
}
