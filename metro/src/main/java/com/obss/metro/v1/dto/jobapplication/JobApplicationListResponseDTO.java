package com.obss.metro.v1.dto.jobapplication;

import com.obss.metro.v1.dto.candidate.CandidateResponseDTO;
import com.obss.metro.v1.entity.JobApplication;

import java.util.UUID;

public record JobApplicationListResponseDTO(UUID id, CandidateResponseDTO applicant) {
  public static JobApplicationListResponseDTO fromJobApplication(
      final JobApplication jobApplication) {
    return new JobApplicationListResponseDTO(
        jobApplication.getId(), CandidateResponseDTO.fromCandidate(jobApplication.getApplicant()));
  }
}
