package com.obss.metro.v1.dto.inuser;

import com.obss.metro.v1.entity.InUser;
import java.util.UUID;

public record InUserResponseDTO(UUID id, String inUrl) {
  public static InUserResponseDTO fromInUser(final InUser inUser) {
    return new InUserResponseDTO(inUser.getId(), inUser.getInUrl());
  }
}
