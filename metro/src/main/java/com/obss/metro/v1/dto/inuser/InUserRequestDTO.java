package com.obss.metro.v1.dto.inuser;

import com.obss.metro.v1.entity.InUser;
import java.util.Arrays;
import java.util.UUID;

public record InUserRequestDTO(String fullName, String profilePicture, String inUrl) {
  public InUser toInUser(final UUID id) {
    final String[] nameArray = fullName.trim().split(" ");
    final String firstName = String.join(" ", Arrays.copyOf(nameArray, nameArray.length - 1));
    final String lastName = nameArray[nameArray.length - 1];

    return InUser.builder()
        .id(id)
        .firstName(firstName)
        .lastName(lastName)
        .profilePicture(profilePicture)
        .inUrl(inUrl)
        .build();
  }
}
