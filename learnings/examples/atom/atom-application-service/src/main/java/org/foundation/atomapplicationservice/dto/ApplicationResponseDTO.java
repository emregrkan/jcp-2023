package org.foundation.atomapplicationservice.dto;

import lombok.Data;
import org.foundation.atomapplicationservice.entity.attributes.*;

import java.util.Date;

@Data
public class ApplicationResponseDTO {
    private String id;
    private ApplicationStatus status;
    private ApplicantDTO applicant;
    private JobListingDTO job;
    private Date createdAt;
}
