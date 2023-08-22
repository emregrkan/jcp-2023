package org.foundation.atomjoblistingservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.foundation.atomjoblistingservice.dto.JobListingFlatResponseDTO;
import org.foundation.atomjoblistingservice.dto.JobListingRequestDTO;
import org.foundation.atomjoblistingservice.dto.JobListingResponseDTO;
import org.foundation.atomjoblistingservice.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public JobListingResponseDTO createJobListing(@RequestBody @NotNull @Valid final JobListingRequestDTO body) {
        return jobListingService.createJobListing(body);
    }
}
