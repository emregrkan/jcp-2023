package org.foundation.atomprofileservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.foundation.atomprofileservice.dto.ProfileRequestDTO;
import org.foundation.atomprofileservice.dto.ProfileResponseDTO;
import org.foundation.atomprofileservice.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/profiles")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping
    public ProfileResponseDTO createProfile(@RequestBody @NotNull @Valid final ProfileRequestDTO profileRequestDTO) throws JsonProcessingException {
        return profileService.createProfile(profileRequestDTO);
    }
}
