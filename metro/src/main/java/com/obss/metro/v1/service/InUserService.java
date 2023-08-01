package com.obss.metro.v1.service;

import com.obss.metro.v1.dto.inuser.InUserPostRequestDTO;
import com.obss.metro.v1.dto.inuser.InUserResponseDTO;
import com.obss.metro.v1.entity.InUser;
import com.obss.metro.v1.exception.impl.ForbiddenException;
import com.obss.metro.v1.exception.impl.ResourceNotFoundException;
import com.obss.metro.v1.repository.InUserRepository;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class InUserService {
  private final InUserRepository userRepository;

  public InUserResponseDTO saveInUser(final InUserPostRequestDTO userDTO, final String id) {
    final InUser user = userDTO.toInUser(UUID.fromString(id));
    return InUserResponseDTO.fromInUser(userRepository.save(user));
  }

  public InUserResponseDTO findInUserById(
      @NotNull final UUID id, @NotNull final Authentication auth) {
    final Optional<InUser> user = userRepository.findById(id);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException("id", "InUser with given id not found");
    }

    final String sub = ((Jwt) auth.getPrincipal()).getSubject();
    if (!Objects.equals(user.get().getId().toString(), sub)
        || auth.getAuthorities().stream()
            .anyMatch(role -> role.getAuthority().equals("ROLE_OPERATOR"))) {
      throw new ForbiddenException();
    }

    return InUserResponseDTO.fromInUser(user.get());
  }
}
