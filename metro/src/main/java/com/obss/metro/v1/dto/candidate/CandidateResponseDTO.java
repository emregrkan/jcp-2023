package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.Candidate;
import java.util.UUID;

public record CandidateResponseDTO(
    UUID id, String firstName, String lastName, String profilePicture, String inUrl) {
  public static CandidateResponseDTO fromCandidate(final Candidate candidate) {
    return new CandidateResponseDTO(
        candidate.getId(),
        candidate.getFirstName(),
        candidate.getLastName(),
        candidate.getProfilePicture(),
        candidate.getInUrl());
  }
}
