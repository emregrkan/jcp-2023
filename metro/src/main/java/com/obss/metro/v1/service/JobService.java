package com.obss.metro.v1.service;

import com.obss.metro.v1.dto.job.JobRequestDTO;
import com.obss.metro.v1.dto.job.JobResponseDTO;
import com.obss.metro.v1.entity.InUser;
import com.obss.metro.v1.entity.Job;
import com.obss.metro.v1.entity.JobApplication;
import com.obss.metro.v1.repository.InUserRepository;
import com.obss.metro.v1.repository.JobApplicationRepository;
import com.obss.metro.v1.repository.JobRepository;
import jakarta.annotation.PostConstruct;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
  private final JobApplicationRepository applicationRepository;

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

  public JobResponseDTO saveJob(JobRequestDTO jobRequestDto) {
    Job job = jobRequestDto.toJob();
    return JobResponseDTO.fromJob(jobRepository.save(job));
  }

  public Optional<JobResponseDTO> findJobById(final Long id) {
    return jobRepository.findById(id).map(JobResponseDTO::fromJob);
  }

  public JobResponseDTO updateJobById(final JobRequestDTO requestDTO, final Long id)
      throws ResponseStatusException {
    final Job job = requestDTO.toJob();
    boolean exists = jobRepository.existsById(id);

    if (exists) {
      job.setId(id);
      return JobResponseDTO.fromJob(jobRepository.save(job));
    }

    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found");
  }

  public void removeJobById(final Long id) {
    jobRepository.removeJobById(id);
  }

  public Set<JobApplication> listJobApplicationsByJobId(final Long id) {
    return applicationRepository.findAllByAppliedJobId(id);
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
  public void test_createJob() throws Exception {
    final Job job =
        Job.builder()
            .id(1238912078414L)
            .title("Java Developer")
            .workplaceType(Job.WorkplaceType.ON_SITE)
            .location("Rome, Italy")
            .type(Job.Type.FULL_TIME)
            .details("We're looking for a Java master with one year experience")
            .status(Job.Status.ACTIVE)
            .dueDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
            .build();

    final InUser user = InUser.builder().id("yrZCpj2Z12").build();

    final JobApplication application =
        JobApplication.builder()
            .id(124135453453L)
            .applicant(user)
            .appliedJob(job)
            .status(JobApplication.Status.SUBMITTED)
            .build();

    jobRepository.save(job);
    userRepository.save(user);
    applicationRepository.save(application);

    final JobRequestDTO requestDto =
        new JobRequestDTO(
            "title",
            Job.WorkplaceType.HYBRID,
            "Afyonkarahisar, Türkiye",
            Job.Type.FULL_TIME,
            Job.Status.ACTIVE,
            Timestamp.valueOf(LocalDateTime.now().plusDays(1)));

    final JobResponseDTO responseDto = saveJob(requestDto);
    log.info(responseDto.toString());
  }
}
