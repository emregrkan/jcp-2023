package com.obss.metro.v1.controller;

import com.obss.metro.v1.dto.inuser.InUserRequestDTO;
import com.obss.metro.v1.dto.inuser.InUserResponseDTO;
import com.obss.metro.v1.service.InUserService;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/in-user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InUserController {
  private final InUserService userService;

  @PostMapping(consumes = "application/json")
  public ResponseEntity<InUserResponseDTO> postUser(
      @NotNull @RequestBody final InUserRequestDTO userDTO,
      @NotNull final Principal principal) {
    final InUserResponseDTO user = userService.saveInUser(userDTO, principal.getName());
    final URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/%s".formatted(user.id()))
            .build()
            .toUri();
    return ResponseEntity.created(location).body(user);
  }

  @GetMapping("/{id}")
  public InUserResponseDTO getUserByInId(@PathVariable UUID id) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userService.findInUserById(id, auth);
  }

  @PutMapping("/{id}")
  public InUserResponseDTO putUserByInId(@PathVariable @NotNull final UUID id, @RequestBody @NotNull final InUserRequestDTO userDTO) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userService.updateInUserById(id, auth, userDTO);
  }
}
