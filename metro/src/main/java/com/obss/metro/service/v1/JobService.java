package com.obss.metro.service.v1;

import com.obss.metro.dto.JobDto;
import com.obss.metro.entity.v1.InUser;
import com.obss.metro.entity.v1.Job;
import com.obss.metro.entity.v1.JobApplication;
import com.obss.metro.repository.v1.InUserRepository;
import com.obss.metro.repository.v1.JobApplicationRepository;
import com.obss.metro.repository.v1.JobRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// todo: convert to `dto`s

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class JobService {
    private final JobRepository jobRepository;
    private final InUserRepository userRepository;
    private final JobApplicationRepository applicationRepository;

    /**
     * @return Set of all jobs
     * @throws Exception if occurred
     */
    public Set<JobDto> findAllJobs() throws Exception {
        return jobRepository
                .findAllBy().stream()
                .map(JobDto::fromJob)
                .collect(Collectors.toSet());
    }

    /**
     * @param page Page number
     * @param jobs Number of job posts in a page
     * @return Set of jobs with given parameters
     * @throws Exception if occurred
     */
    public Set<JobDto> findJobsPaged(int page, int jobs) throws Exception {
        final Pageable pageRequest = PageRequest.of(page, jobs);
        return jobRepository
                .findAll(pageRequest).stream()
                .map(JobDto::fromJob)
                .collect(Collectors.toSet());
    }

    public JobDto saveJob(JobDto jobDto) throws Exception {
        final Job job = jobRepository.save(jobDto.toJob());
        return JobDto.fromJob(job);
    }

    public Optional<JobDto> findJobById(final Long id) throws Exception {
        return jobRepository
                .findById(id).map(JobDto::fromJob);
    }

    public Set<JobApplication> listJobApplicationsByJobId(final Long id) throws Exception {
        return applicationRepository.findAllByAppliedJobId(id);
    }


    /**
     *
     * Scheduled: Runs {@link JobRepository#findAndUpdateActiveExpired()} every one hour automatically; marks the expired Jobs
     * @throws Exception if occurred
     * @see com.obss.metro.entity.v1.Job
     */
    @Scheduled(cron = "0 * * * *")
    @Transactional
    public void updateExpired() throws Exception {
        jobRepository
                .findAndUpdateActiveExpired();
    }

    @PostConstruct
    public void test_createJob() throws Exception {
        final Job job = Job.builder().id(124123123124L).title("Java Developer").workplaceType(Job.WorkplaceType.ON_SITE).location("Rome, Italy").type(Job.Type.FULL_TIME).details("Hello, we're looking for a fucking Java master with one year experience").status(Job.Status.ACTIVE).dueDate(Timestamp.valueOf(LocalDateTime.now().plusDays(30))).build();

        final InUser user = InUser.builder().id("yrZCpj2Z12").build();

        final JobApplication application = JobApplication.builder().id(124135453453L).applicant(user).appliedJob(job).status(JobApplication.Status.SUBMITTED).build();

        jobRepository.save(job);
        userRepository.save(user);
        applicationRepository.save(application);
    }
}
