package com.obss.metro.v1.service;

import com.obss.metro.v1.dto.job.JobRequestDTO;
import com.obss.metro.v1.dto.job.JobResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationListResponseDTO;
import com.obss.metro.v1.entity.InUser;
import com.obss.metro.v1.entity.Job;
import com.obss.metro.v1.entity.JobApplication;
import com.obss.metro.v1.exception.impl.ForbiddenException;
import com.obss.metro.v1.exception.impl.ResourceNotFoundException;
import com.obss.metro.v1.repository.InUserRepository;
import com.obss.metro.v1.repository.JobRepository;
import jakarta.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

// todo: convert to `dto`s

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JobService {
  private final JobRepository jobRepository;
  private final InUserRepository userRepository;

  /**
   * @param page Page number
   * @param jobs Number of job posts in a page
   * @return Set of jobs with given parameters
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  public Page<JobResponseDTO> findPagedJobs(int page, int jobs) {
    final Pageable pageRequest = PageRequest.of(page, jobs);
    return jobRepository.findAll(pageRequest).map(JobResponseDTO::fromJob);
  }

  public JobResponseDTO saveJob(final JobRequestDTO jobRequestDto, final UUID posterId) {
    Job job = jobRequestDto.toJob();
    job.setPosterId(posterId);
    return JobResponseDTO.fromJob(jobRepository.save(job));
  }

  public JobResponseDTO findJobById(final Long id) {
    Optional<Job> byId = jobRepository.findById(id);
    return JobResponseDTO.fromJob(
        byId.orElseThrow(
            () -> new ResourceNotFoundException("id", "Resource with requested id not found")));
  }

  public JobResponseDTO updateJobById(
      final JobRequestDTO requestDTO, final Long id, final UUID posterId)
      throws ResponseStatusException {
    final Job job = requestDTO.toJob();
    final Optional<UUID> target = jobRepository.findPosterIdById(id);

    if (target.isPresent()) {
      if (!target.get().equals(posterId)) {
        throw new ForbiddenException();
      }

      job.setId(id);
      job.setPosterId(posterId);
      return JobResponseDTO.fromJob(jobRepository.save(job));
    }

    throw new ResourceNotFoundException("id", "Resource with requested id not found");
  }

  public void removeJobById(final Long id, final UUID posterId) {
    final Optional<UUID> target = jobRepository.findPosterIdById(id);

    if (target.isPresent()) {
      if (!target.get().equals(posterId)) {
        throw new ForbiddenException();
      }

      jobRepository.removeJobById(id);
    }

    throw new ResourceNotFoundException("id", "Resource with requested id not found");
  }

  public Set<JobApplicationListResponseDTO> findAllApplicationsById(final Long id) {
    return jobRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("id", "Resource with requested id not found"))
        .getApplications()
        .stream()
        .map(JobApplicationListResponseDTO::fromJobApplication)
        .collect(Collectors.toSet());
  }

  /**
   * Scheduled: Runs {@link JobRepository#findAndUpdateActiveExpired()} top of every hour
   * automatically; marks the expired Jobs
   *
   * @see Job
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  @Scheduled(cron = "0 0 * * * *")
  public void updateExpired() {
    jobRepository.findAndUpdateActiveExpired();
  }

  @PostConstruct
  public void test_createJob() {
    final Job job =
        Job.builder()
            .id(6942031911L)
            // todo: probably a new id is required
            .posterId(UUID.fromString("c73e7f0f-a36f-49c8-9cce-2c2f9a80e80e"))
            .title("Java Developer")
            .workplaceType(Job.WorkplaceType.ON_SITE)
            .location("Rome, Italy")
            .type(Job.Type.FULL_TIME)
            .details("We're looking for a Java master with one year experience")
            .status(Job.Status.ACTIVE)
            .dueDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
            .build();

    final InUser user =
        InUser.builder()
            .id(UUID.fromString("8bd0c13d-bf59-4c66-a3cf-699f978f910d"))
            .build();

    user.addJobApplication(
        new JobApplication(
            ThreadLocalRandom.current().nextLong(),
            user,
            jobRepository.save(job),
            JobApplication.Status.SUBMITTED));
    userRepository.save(user);
  }
}
