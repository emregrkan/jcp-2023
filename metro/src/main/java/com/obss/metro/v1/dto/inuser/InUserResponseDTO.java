package com.obss.metro.v1.dto.inuser;

import com.obss.metro.v1.entity.InUser;

public record InUserResponseDTO(Long id, String inId) {
  public static InUserResponseDTO fromInUser(final InUser inUser) {
    return new InUserResponseDTO(inUser.getId(), inUser.getInId());
  }
}
