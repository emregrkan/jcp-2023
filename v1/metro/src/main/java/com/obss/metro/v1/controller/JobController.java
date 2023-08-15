package com.obss.metro.v1.controller;

import com.obss.metro.v1.configuration.SecurityConfiguration;
import com.obss.metro.v1.dto.candidate.CandidateAuthResponseDTO;
import com.obss.metro.v1.dto.job.JobRequestDTO;
import com.obss.metro.v1.dto.job.JobResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationListResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationResponseDTO;
import com.obss.metro.v1.exception.impl.ServerException;
import com.obss.metro.v1.service.EmailService;
import com.obss.metro.v1.service.JobService;
import com.obss.metro.v1.service.SearchService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.security.Principal;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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
  private final EmailService emailService;
  private final SearchService searchService;

  /**
   * @param page Optional query parameter for page number with default value of 1
   * @param jobs Optional query parameter for number of jobs in a page with default value of 9
   * @return Set of jobs
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  @GetMapping
  public Page<JobResponseDTO> getAllJobsPaged(
      @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> jobs) {
    // 0 00 -> no args
    // 1 01 -> only jobs
    // 2 10 -> only page
    // 3 11 -> both present
    int status = (page.isPresent() ? 1 : 0) << 1;
    status ^= (jobs.isPresent() ? 1 : 0);

    log.debug("status: %d".formatted(status));

    return switch (status) {
      case 0 -> jobService.findPagedJobs(0, 9);
      case 1 -> jobService.findPagedJobs(0, jobs.get());
      case 2 -> jobService.findPagedJobs(page.get(), 9);
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
  public ResponseEntity<JobResponseDTO> postJob(@RequestBody JobRequestDTO jobRequestDTO) {
    // todo: return response entity from service?

    final JobResponseDTO responseDTO = jobService.saveJob(jobRequestDTO);
    final URI location =
        ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/%s".formatted(responseDTO.id()))
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
  public JobResponseDTO putJobById(@RequestBody JobRequestDTO requestDTO, @PathVariable UUID id) {
    return jobService.updateJobById(requestDTO, id);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('OPERATOR')")
  public void deleteJobById(@PathVariable UUID id) {
    jobService.removeJobById(id);
  }

  @PostMapping("/{id}/status")
  @PreAuthorize("hasRole('OPERATOR')")
  public void closeJobById(@PathVariable UUID id, @RequestBody Map<String, String> body) {
    jobService.closeJobById(id, body.get("status"));
  }

  @PostMapping("/{id}/applications")
  @PreAuthorize("!hasRole('OPERATOR')")
  public JobApplicationResponseDTO postApplicationById(
      @PathVariable UUID id, @NotNull Principal principal) {
    final JobApplicationResponseDTO response =
        jobService.saveJobApplicationById(id, principal.getName());
    final String email =
        ((Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getClaimAsString("email");
    final String message =
        """
Hello,

Thank you for your interest in a career at OBSS. We have received your application to %s at %s.

You will be hearing from us!

-----------------------------------------------------------------------------------------------

This message sent automatically.
For any questions or feedbacks reply to <a href="mailto:hr@obss.tech">OBSS Human Resources</a>
            """;
    emailService.sendEmailNotification(
        email,
        "Your Application",
        message.formatted(response.jobApplied().title(), response.jobApplied().location()));
    return response;
  }

  @GetMapping("/{id}/applications")
  @PreAuthorize("hasRole('OPERATOR')")
  public Set<JobApplicationListResponseDTO> listJobApplicationsByJobId(@PathVariable UUID id) {
    return jobService.findAllApplicationsById(id);
  }

  @PostMapping("/{jobId}/applications/{applicationId}/status")
  @PreAuthorize("hasRole('OPERATOR')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void setJobApplicationStatusById(
      @PathVariable UUID applicationId,
      @PathVariable UUID jobId,
      @RequestBody Map<String, String> body) {
    JobApplicationResponseDTO response =
        jobService.setJobApplicationStatusById(jobId, applicationId, body.get("status"));
    final String message =
        """
Hello,

We would like to inform you that we have viewed your application to %s at %s.

We will be in touch soon!

-----------------------------------------------------------------------------------------------

This message sent automatically.
For any questions or feedbacks reply to <a href="mailto:hr@obss.tech">OBSS Human Resources</a>
                """;
    emailService.sendEmailNotification(
        response.applicant().email(),
        "Your Application",
        message.formatted(response.jobApplied().title(), response.jobApplied().location()));
  }

  @GetMapping("/{id}/details")
  public String getJobDetails(@PathVariable UUID id) {
    return jobService.findJobDetailsById(id);
  }

  @PostMapping("/{id}/details")
  @PreAuthorize("hasRole('OPERATOR')")
  public void postJobDetails(@PathVariable UUID id, @RequestBody Map<String, String> body) {
    jobService.saveJobDetailsById(id, body.get("details"));
  }
}
