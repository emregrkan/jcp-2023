package org.foundation.atomjoblistingservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ApplicationForJobListingDTO {
    private String id;
    private String status;
    private ApplicantResponseDTO applicant;
    private Date createdAt;
}
