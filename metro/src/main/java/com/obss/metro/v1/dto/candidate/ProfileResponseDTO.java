package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.Candidate;
import com.obss.metro.v1.entity.CandidateEducation;
import com.obss.metro.v1.entity.CandidateExperience;

import java.util.List;

public record ProfileResponseDTO(
    String id,
    String url,
    String headline,
    String location,
    String about,
    List<CandidateExperienceDTO> experience,
    List<CandidateEducationDTO> education) {

  public static ProfileResponseDTO fromCandidate(final Candidate candidate) {
    return new ProfileResponseDTO(
        candidate.getId().toString(),
        candidate.getInUrl(),
        candidate.getHeadline(),
        candidate.getLocation(),
        candidate.getAbout(),
        candidate.getExperience().stream()
            .map(CandidateExperienceDTO::fromCandidateExperience)
            .toList(),
        candidate.getEducation().stream()
            .map(CandidateEducationDTO::fromCandidateEducation)
            .toList());
  }
}
