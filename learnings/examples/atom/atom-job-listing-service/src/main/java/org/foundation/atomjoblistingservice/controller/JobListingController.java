package org.foundation.atomjoblistingservice.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.foundation.atomjoblistingservice.dto.JobListingFlatResponseDTO;
import org.foundation.atomjoblistingservice.dto.JobListingRequestDTO;
import org.foundation.atomjoblistingservice.service.JobListingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v2/job-listings")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JobListingController {
    private final JobListingService jobListingService;

    @GetMapping
    public Page<JobListingFlatResponseDTO> findAllJobListings(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {
        return jobListingService.findAllJobListings(page, size);
    }

    @PostMapping
    public JobListingFlatResponseDTO createJobListing(@RequestBody @NotNull @Valid final JobListingRequestDTO body) {
        return jobListingService.createJobListing(body);
    }
}
