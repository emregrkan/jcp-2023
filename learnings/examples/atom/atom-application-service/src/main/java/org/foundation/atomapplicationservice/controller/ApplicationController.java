package org.foundation.atomapplicationservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.foundation.atomapplicationservice.dto.ApplicationRequestDTO;
import org.foundation.atomapplicationservice.dto.ApplicationResponseDTO;
import org.foundation.atomapplicationservice.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/applications")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    public ApplicationResponseDTO createApplication(@RequestBody @NotNull @Valid final ApplicationRequestDTO applicationRequestDTO) throws JsonProcessingException {
        return applicationService.createApplication(applicationRequestDTO);
    }
}
