package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.Candidate;

import java.util.UUID;

public record CandidateAuthResponseDTO(
    UUID id, String firstName, String lastName, String email, String profilePicture, String inUrl) {
  public static CandidateAuthResponseDTO fromCandidate(final Candidate candidate) {
    return new CandidateAuthResponseDTO(
        candidate.getId(),
        candidate.getFirstName(),
        candidate.getLastName(),
        candidate.getEmail(),
        candidate.getProfilePicture(), candidate.getInUrl());
  }
}
