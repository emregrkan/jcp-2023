package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.dto.job.JobResponseDTO;
import com.obss.metro.v1.entity.JobApplication;
import java.util.UUID;

public record CandidateJobApplicationDTO(
    UUID id, JobApplication.Status status, JobResponseDTO jobApplied) {
  public static CandidateJobApplicationDTO fromJobApplication(final JobApplication application) {
    return new CandidateJobApplicationDTO(
        application.getId(),
        application.getStatus(),
        JobResponseDTO.fromJob(application.getJobApplied()));
  }
}
