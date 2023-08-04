package com.obss.metro.v1.controller;

import com.obss.metro.v1.configuration.SecurityConfiguration;
import com.obss.metro.v1.dto.job.JobRequestDTO;
import com.obss.metro.v1.dto.job.JobResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationListResponseDTO;
import com.obss.metro.v1.exception.impl.ServerException;
import com.obss.metro.v1.service.JobService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * /api/v1/jobs .GET? .POST? /api/v1/jobs/:id .GET. PUT PATCH DELETE /api/v1/jobs/:id/details ?
 * /api/v1/jobs/:id/applicants GET todo: blacklist?
 */
@RestController
@RequestMapping(value = "/api/v1/jobs")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SecurityRequirement(name = SecurityConfiguration.SECURITY_CONFIG_NAME)
@Slf4j
public class JobController {
  private final JobService jobService;

  /**
   * @param page Optional query parameter for page number with default value of 1
   * @param jobs Optional query parameter for number of jobs in a page with default value of 8
   * @return Set of jobs
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  @GetMapping
  public Page<JobResponseDTO> getJobs(
      @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> jobs) {
    // 0 00 -> no args
    // 1 01 -> only jobs
    // 2 10 -> only page
    // 3 11 -> both present
    int status = (page.isPresent() ? 1 : 0) << 1;
    status ^= (jobs.isPresent() ? 1 : 0);

    log.debug("status: %d".formatted(status));

    return switch (status) {
      case 0 -> jobService.findPagedJobs(0, 8);
      case 1 -> jobService.findPagedJobs(0, jobs.get());
      case 2 -> jobService.findPagedJobs(page.get(), 8);
      case 3 -> jobService.findPagedJobs(page.get(), jobs.get());
      default -> throw new ServerException();
    };
  }

  /**
   * @param jobRequestDTO {@link JobRequestDTO} object parsed from request body which is
   *     "application/json"
   * @return {@link ResponseEntity} of {@link JobResponseDTO} with status 201 on success
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  @PostMapping(consumes = "application/json")
  @PreAuthorize("hasRole('OPERATOR')")
  public ResponseEntity<JobResponseDTO> postJob(
      @RequestBody JobRequestDTO jobRequestDTO, @NotNull final Principal principal) {
    // todo: return response entity from service?

    final JobResponseDTO responseDTO =
        jobService.saveJob(jobRequestDTO, UUID.fromString(principal.getName()));
    final URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/%d".formatted(responseDTO.id()))
            .build()
            .toUri();
    return ResponseEntity.created(location).body(responseDTO);
  }

  @GetMapping("/{id}")
  public JobResponseDTO getJobById(@PathVariable UUID id) {
    return jobService.findJobById(id);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('OPERATOR')")
  public JobResponseDTO putJobById(
      @RequestBody JobRequestDTO requestDTO,
      @PathVariable UUID id,
      @NotNull final Principal principal) {
    return jobService.updateJobById(requestDTO, id, UUID.fromString(principal.getName()));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('OPERATOR')")
  public void deleteJobById(@PathVariable UUID id, @NotNull final Principal principal) {
    jobService.removeJobById(id, UUID.fromString(principal.getName()));
  }

  @GetMapping("/{id}/applications")
  public Set<JobApplicationListResponseDTO> listJobApplicationsByJobId(@PathVariable UUID id) {
    return jobService.findAllApplicationsById(id);
  }
}
