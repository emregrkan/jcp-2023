package com.obss.metro.v1.dto.jobapplication;

import com.obss.metro.v1.dto.candidate.CandidateAuthResponseDTO;
import com.obss.metro.v1.dto.job.JobResponseDTO;
import com.obss.metro.v1.entity.JobApplication;
import java.util.UUID;

public record JobApplicationResponseDTO(
        UUID id, JobApplication.Status status, JobResponseDTO jobApplied, CandidateAuthResponseDTO applicant) {
  public static JobApplicationResponseDTO fromJobApplication(final JobApplication application) {
    return new JobApplicationResponseDTO(
            application.getId(),
        application.getStatus(),
        JobResponseDTO.fromJob(application.getJobApplied()),
        CandidateAuthResponseDTO.fromCandidate(application.getApplicant()));
  }
}
