package org.foundation.atomapplicantservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.foundation.atomapplicantservice.dto.ApplicantRequestDTO;
import org.foundation.atomapplicantservice.dto.ApplicantResponseDTO;
import org.foundation.atomapplicantservice.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v2/applicants")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicantController {
    private final ApplicantService applicantService;

    @PostMapping
    public ApplicantResponseDTO createApplicant(@RequestBody @NotNull @Valid final ApplicantRequestDTO applicantRequestDTO) throws JsonProcessingException {
        return applicantService.createApplicant(applicantRequestDTO);
    }

    @GetMapping("/{id}")
    public ApplicantResponseDTO findApplicantByID(@PathVariable @NotNull final UUID id) {
        return applicantService.findApplicantByID(id);
    }
}
