package com.obss.metro.v1.service;

import com.obss.metro.v1.dto.job.JobRequestDTO;
import com.obss.metro.v1.dto.job.JobResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationListResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationResponseDTO;
import com.obss.metro.v1.entity.Candidate;
import com.obss.metro.v1.entity.Job;
import com.obss.metro.v1.entity.JobApplication;
import com.obss.metro.v1.exception.impl.ResourceNotFoundException;
import com.obss.metro.v1.repository.CandidateRepository;
import com.obss.metro.v1.repository.DepartmentRepository;
import com.obss.metro.v1.repository.JobRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
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
  private final CandidateRepository candidateRepository;
  private final DepartmentRepository departmentRepository;

  /**
   * @param page Page numberFr
   * @param jobs Number of job posts in a page
   * @return Set of jobs with given parameters
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  public Page<JobResponseDTO> findPagedJobs(int page, int jobs) {
    final Pageable pageRequest = PageRequest.of(page, jobs);
    return jobRepository.findAll(pageRequest).map(JobResponseDTO::fromJob);
  }

  public JobResponseDTO saveJob(final JobRequestDTO jobRequestDTO) {
    Job job = jobRequestDTO.toJob();
    job.setDepartment(departmentRepository.getReferenceById(jobRequestDTO.departmentId()));
    return JobResponseDTO.fromJob(jobRepository.save(job));
  }

  public JobResponseDTO findJobById(final UUID id) {
    Optional<Job> byId = jobRepository.findById(id);
    return JobResponseDTO.fromJob(
        byId.orElseThrow(
            () -> new ResourceNotFoundException("id", "Resource with requested id not found")));
  }

  public JobResponseDTO updateJobById(
      final JobRequestDTO requestDTO, final UUID id)
      throws ResponseStatusException {
    final Job job = requestDTO.toJob();
    final Optional<Job> target = jobRepository.findById(id);

    if (target.isPresent()) {
      job.setId(id);
      job.setDepartment(target.get().getDepartment());
      return JobResponseDTO.fromJob(jobRepository.save(job));
    }

    throw new ResourceNotFoundException("id", "Resource with requested id not found");
  }

  public void removeJobById(final UUID id) {
    jobRepository.removeJobById(id);
  }

  public Set<JobApplicationListResponseDTO> findAllApplicationsById(final UUID id) {
    return jobRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("id", "Resource with requested id not found"))
        .getApplications()
        .stream()
        .map(JobApplicationListResponseDTO::fromJobApplication)
        .collect(Collectors.toSet());
  }

  public String findJobDetailsById(final UUID id) {
    final Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    return job.getDetails();
  }

  public void saveJobDetailsById(final UUID id, final String details) {
    final Job job = jobRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    job.setDetails(details);
    jobRepository.save(job);
  }

  @Transactional
  public JobApplicationResponseDTO setJobApplicationStatusById(final UUID jobId, final UUID applicationId, final String status) {
    final Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    final JobApplication application = job.getApplications().stream().filter(app -> app.getId().toString().equals(applicationId.toString())).findAny().orElseThrow(() -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    application.setStatus(JobApplication.Status.valueOf(status));
    return JobApplicationResponseDTO.fromJobApplication(application);
  }

  public JobApplicationResponseDTO saveJobApplicationById(final UUID jobId, final String userId) {
    final JobApplication application = new JobApplication();
    final Job jobApplied = jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    final Set<JobApplication> applications = jobApplied.getApplications();
    final Candidate applicant = candidateRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new ResourceNotFoundException("id", "Resource with requested id not found"));
    application.setId(null);
    application.setApplicant(applicant);
    application.setJobApplied(jobApplied);
    application.setStatus(JobApplication.Status.SUBMITTED);

    applications.add(application);
    jobRepository.save(jobApplied);
    
    return JobApplicationResponseDTO.fromJobApplication(application);
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

//  @PostConstruct
//  public void test_createJob() {
//    final Job job =
//        Job.builder()
//            .id(null)
//            // todo: probably a new id is required
//            .posterId(UUID.fromString("c73e7f0f-a36f-49c8-9cce-2c2f9a80e80e"))
//            .title("Java Developer")
//            .workplaceType(Job.WorkplaceType.ON_SITE)
//            .location("Rome, Italy")
//            .type(Job.Type.FULL_TIME)
//            .details("We're looking for a Java master with one year experience")
//            .status(Job.Status.ACTIVE)
//            .dueDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30)))
//            .build();
//
//    final Candidate user =
//        Candidate.builder().id(UUID.fromString("8ad0c13d-bf59-4c66-a3cf-699f978f910d")).build();
//
//    user.addJobApplication(
//        new JobApplication(null, user, jobRepository.save(job), JobApplication.Status.SUBMITTED));
//    userRepository.save(user);
//  }
}
