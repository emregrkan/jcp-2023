package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.Candidate;
import java.util.Arrays;
import java.util.UUID;

public record CandidateRequestDTO(
    String fullName, String email, String profilePicture, String inUrl) {
  public Candidate toCandidate(final UUID id) {
    final String[] nameArray = fullName.trim().split(" ");
    final String firstName = String.join(" ", Arrays.copyOf(nameArray, nameArray.length - 1));
    final String lastName = nameArray[nameArray.length - 1];

    return Candidate.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .profilePicture(profilePicture)
        .inUrl(inUrl)
        .build();
  }
}
