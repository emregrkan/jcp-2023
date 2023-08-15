package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.Candidate;

import java.util.UUID;

public record CandidateAuthRequestDTO(
        String firstName, String lastName, String email, String profilePicture
) {
    public Candidate toCandidate(final UUID id) {
        return Candidate.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .profilePicture(profilePicture)
                .build();
    }
}
