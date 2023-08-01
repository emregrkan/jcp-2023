package com.obss.metro.v1.dto.inuser;

import com.obss.metro.v1.entity.InUser;
import java.util.UUID;

public record InUserPostRequestDTO(String inUrl) {
  public InUser toInUser(final UUID id) {
    return InUser.builder().id(id).inUrl(inUrl).build();
  }
}
