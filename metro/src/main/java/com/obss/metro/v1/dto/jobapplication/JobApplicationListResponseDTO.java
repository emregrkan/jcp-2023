package com.obss.metro.v1.dto.jobapplication;

import com.obss.metro.v1.dto.candidate.CandidateAuthResponseDTO;
import com.obss.metro.v1.entity.JobApplication;

import java.util.UUID;

public record JobApplicationListResponseDTO(UUID id, JobApplication.Status status, CandidateAuthResponseDTO applicant) {
  public static JobApplicationListResponseDTO fromJobApplication(
      final JobApplication jobApplication) {
    return new JobApplicationListResponseDTO(
        jobApplication.getId(), jobApplication.getStatus(), CandidateAuthResponseDTO.fromCandidate(jobApplication.getApplicant()));
  }
}
