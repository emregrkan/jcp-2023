package org.foundation.atomapplicantservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.foundation.atomapplicantservice.dto.ApplicantRequestDTO;
import org.foundation.atomapplicantservice.dto.ApplicantResponseDTO;
import org.foundation.atomapplicantservice.entity.attributes.Application;
import org.foundation.atomapplicantservice.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicantController {
    private final ApplicantService applicantService;

    @PostMapping
    public ApplicantResponseDTO createApplicant(@RequestBody @NotNull @Valid final ApplicantRequestDTO applicantRequestDTO) throws JsonProcessingException {
        return applicantService.createApplicant(applicantRequestDTO);
    }

    @GetMapping("/{id}")
    public ApplicantResponseDTO findApplicantById(@PathVariable @NotNull final UUID id) {
        return applicantService.findApplicantById(id);
    }

    @PutMapping("/{id}")
    public ApplicantResponseDTO updateApplicantById(@PathVariable @NotNull final UUID id, @RequestBody @NotNull @Valid ApplicantRequestDTO applicantRequestDTO) throws JsonProcessingException {
        return applicantService.updateApplicantById(id, applicantRequestDTO);
    }

    @GetMapping("/{id}/applications")
    public Page<Application> findApplicationsById(@PathVariable @NotNull final UUID id, @RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "9") final int size) {
        return applicantService.findApplicationsById(id, page, size);
    }

    @GetMapping
    // @Authorized
    public Page<ApplicantResponseDTO> findAllApplicants(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        return applicantService.findAllApplicants(page, size);
    }
}
