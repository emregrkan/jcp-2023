package com.obss.metro.controller.v1;

import com.obss.metro.dto.JobDto;
import com.obss.metro.entity.v1.JobApplication;
import com.obss.metro.service.v1.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

/**
 * /api/v1/jobs .GET? .POST?
 * /api/v1/jobs/:id .GET. PUT PATCH DELETE
 * /api/v1/jobs/:id/details ?
 * /api/v1/jobs/:id/applicants GET
 * todo: blacklist?
 */

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JobController {
    private final JobService jobService;

    /**
     * @param page Optional query parameter for
     *             page number with default value of 1
     * @param jobs Optional query parameter for
     *             number of jobs in a page with default value of 25
     * @return Set of jobs
     * @throws Exception if occurred
     */
    @GetMapping
    public Set<JobDto> getJobs(@RequestParam Optional<Integer> page,
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

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public JobDto postJob(JobDto jobDto) throws Exception {
        return jobService.saveJob(jobDto);
    }

    @GetMapping("/{id}")
    public JobDto findJobById(@PathVariable Long id) throws Exception {
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
