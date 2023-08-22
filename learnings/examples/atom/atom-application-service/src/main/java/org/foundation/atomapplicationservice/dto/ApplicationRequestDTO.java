package org.foundation.atomapplicationservice.dto;

import jakarta.validation.Valid;
import lombok.Data;
import org.foundation.atomapplicationservice.entity.attributes.ApplicationStatus;

@Data
public class ApplicationRequestDTO {
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;
    @Valid
    private ApplicantDTO applicant;
    @Valid
    private JobListingDTO job;
}
