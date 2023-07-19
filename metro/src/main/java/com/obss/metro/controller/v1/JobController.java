package com.obss.metro.controller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.obss.metro.dto.v1.job.JobRequestDTO;
import com.obss.metro.dto.v1.job.JobResponseDTO;
import com.obss.metro.entity.v1.JobApplication;
import com.obss.metro.service.v1.JobService;
import jakarta.persistence.RollbackException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * /api/v1/jobs .GET? .POST?
 * /api/v1/jobs/:id .GET. PUT PATCH DELETE
 * /api/v1/jobs/:id/details ?
 * /api/v1/jobs/:id/applicants GET
 * todo: blacklist?
 */

@RestController
@RequestMapping(value = "/api/v1/jobs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JobController {
    private final JobService jobService;
    private final ObjectMapper objectMapper;

    /**
     * @param page Optional query parameter for
     *             page number with default value of 1
     * @param jobs Optional query parameter for
     *             number of jobs in a page with default value of 25
     * @return Set of jobs
     * @throws Exception if occurred
     */
    @GetMapping
    public Set<JobResponseDTO> getJobs(@RequestParam Optional<Integer> page,
                                       @RequestParam Optional<Integer> jobs) throws Exception {

        // 0 00 -> no args
        // 1 01 -> only jobs
        // 2 10 -> only page
        // 3 11 -> both present
        int status = (page.isPresent() ? 1 : 0) << 1;
        status ^= (jobs.isPresent() ? 1 : 0);

        log.debug("status: %d".formatted(status));

        return switch (status) {
            case 0 -> jobService.findAllJobs();
            // todo: how many pages(metadata)?
            case 1 -> jobService.findJobsPaged(0, jobs.get());
            case 2 -> jobService.findJobsPaged(page.get(), 25);
            case 3 -> jobService.findJobsPaged(page.get(), jobs.get());
            default -> throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        };
    }

    @PostMapping(consumes = "application/json")
    public JobResponseDTO postJob(@RequestBody JobRequestDTO jobResponseDto) {
        return jobService.saveJob(jobResponseDto);
    }

    @GetMapping("/{id}")
    public JobResponseDTO findJobById(@PathVariable Long id) throws Exception {
        return jobService
                .findJobById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found")
                );
    }

    @GetMapping("/{id}/applicants")
    public Set<JobApplication> listJobApplicationsByJobId(@PathVariable Long id) throws Exception {
        return jobService.listJobApplicationsByJobId(id);
    }
}
