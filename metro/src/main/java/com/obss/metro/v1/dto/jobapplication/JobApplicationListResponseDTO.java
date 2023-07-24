package com.obss.metro.v1.dto.jobapplication;

import com.obss.metro.v1.dto.inuser.InUserResponseDTO;
import com.obss.metro.v1.entity.JobApplication;

public record JobApplicationListResponseDTO(Long id, InUserResponseDTO applicant) {
  public static JobApplicationListResponseDTO fromJobApplication(
      final JobApplication jobApplication) {
    return new JobApplicationListResponseDTO(
        jobApplication.getId(), InUserResponseDTO.fromInUser(jobApplication.getApplicant()));
  }
}
