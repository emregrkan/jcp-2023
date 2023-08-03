package com.obss.metro.v1.controller;

import com.obss.metro.v1.dto.inuser.InUserRequestDTO;
import com.obss.metro.v1.dto.inuser.InUserResponseDTO;
import com.obss.metro.v1.service.InUserService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/in-user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class InUserController {
  private final InUserService userService;
  private final RabbitTemplate rabbitTemplate;

//  @RabbitListener(queues = "q.user-profile")
//  public void profileListener(Object profile) {
//    log.info(profile.toString());
//  }

  @PostMapping(consumes = "application/json")
  public ResponseEntity<String> postUser(
      @NotNull @RequestBody final InUserRequestDTO userDTO, @NotNull final Principal principal) {
    final InUserResponseDTO user = userService.saveInUser(userDTO, principal.getName());
    final URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/%s".formatted(user.id()))
            .build()
            .toUri();

    return ResponseEntity.created(location).body("ok");
  }

  @GetMapping("/{id}")
  public InUserResponseDTO getUserByInId(@PathVariable UUID id) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return userService.findInUserById(id, auth);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> putUserByInId(
      @PathVariable @NotNull final UUID id, @RequestBody @NotNull final InUserRequestDTO userDTO) {
    final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    userService.updateInUserById(id, auth, userDTO);
    rabbitTemplate.convertAndSend("q.user-url", userDTO.inUrl());
    return ResponseEntity.status(200).build();
  }
}
