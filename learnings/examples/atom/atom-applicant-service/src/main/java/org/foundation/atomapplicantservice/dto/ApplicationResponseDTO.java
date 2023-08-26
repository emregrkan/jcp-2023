package org.foundation.atomapplicantservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ApplicationResponseDTO {
    private String id;
    private String status;
    private ApplicantResponseDTO applicant;
    private JobListingResponseDTO job;
    private Date createdAt;
}
