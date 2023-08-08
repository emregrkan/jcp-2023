package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.Candidate;
import java.util.List;
import java.util.UUID;

public record CandidateFullResponseDTO(
    UUID id,
    String firstName,
    String lastName,
    String email,
    String profilePicture,
    String headline,
    String location,
    String about,
    String inUrl,
    List<CandidateExperienceDTO> experience,
    List<CandidateEducationDTO> education,
    List<CandidateJobApplicationDTO> applications) {
  public static CandidateFullResponseDTO fromCandidate(final Candidate candidate) {
    return new CandidateFullResponseDTO(
        candidate.getId(),
        candidate.getFirstName(),
        candidate.getLastName(),
        candidate.getEmail(),
        candidate.getProfilePicture(),
        candidate.getHeadline(),
        candidate.getLocation(),
        candidate.getAbout(),
        candidate.getInUrl(),
        candidate.getExperience().stream()
            .map(CandidateExperienceDTO::fromCandidateExperience)
            .toList(),
        candidate.getEducation().stream()
            .map(CandidateEducationDTO::fromCandidateEducation)
            .toList(),
        candidate.getApplications().stream()
            .map(CandidateJobApplicationDTO::fromJobApplication)
            .toList());
  }
}
