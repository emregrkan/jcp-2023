package org.foundation.atomjoblistingservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.foundation.atomjoblistingservice.dto.ApplicationForJobListingDTO;
import org.foundation.atomjoblistingservice.dto.JobListingFlatResponseDTO;
import org.foundation.atomjoblistingservice.dto.JobListingRequestDTO;
import org.foundation.atomjoblistingservice.dto.JobListingResponseDTO;
import org.foundation.atomjoblistingservice.entity.attributes.Application;
import org.foundation.atomjoblistingservice.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobListingController {
    private final JobListingService jobListingService;

    @GetMapping
    public Page<JobListingFlatResponseDTO> findAllJobListings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        return jobListingService.findAllJobListings(page, size);
    }

    @GetMapping("/{id}")
    public JobListingResponseDTO findJobListingById(@PathVariable @NotBlank final String id) {
        return jobListingService.findJobListingById(id);
    }

    @GetMapping("/{id}/applications")
    // @Authorized
    public Page<Application> findApplicationsByJobId(@PathVariable @NotBlank final String id, @RequestParam(defaultValue = "0") final int page, @RequestParam(defaultValue = "9") final int size) {
        return jobListingService.findApplicationsByJobId(id, page, size);
    }

    @PostMapping
    public JobListingResponseDTO createJobListing(@RequestBody @NotNull @Valid final JobListingRequestDTO jobListingRequestDTO) {
        return jobListingService.createJobListing(jobListingRequestDTO);
    }

    @PutMapping("/{id}")
    public JobListingResponseDTO updateJobListingById(@PathVariable @NotBlank final String id, @RequestBody @NotNull final JobListingRequestDTO jobListingRequestDTO) throws JsonProcessingException {
        return jobListingService.updateJobListingById(id, jobListingRequestDTO);
    }
}
