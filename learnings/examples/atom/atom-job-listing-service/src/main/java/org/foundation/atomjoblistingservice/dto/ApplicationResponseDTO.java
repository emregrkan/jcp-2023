package org.foundation.atomjoblistingservice.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ApplicationResponseDTO {
    private String id;
    private String status;
    private ApplicantResponseDTO applicant;
    private JobListingFlatResponseDTO job;
    private Date createdAt;
}
