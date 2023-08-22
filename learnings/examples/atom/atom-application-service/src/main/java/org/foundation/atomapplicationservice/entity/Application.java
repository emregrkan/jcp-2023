package org.foundation.atomapplicationservice.entity;

import lombok.Data;
import org.foundation.atomapplicationservice.dto.ApplicantDTO;
import org.foundation.atomapplicationservice.dto.JobListingDTO;
import org.foundation.atomapplicationservice.entity.attributes.ApplicationStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
public class Application {
    @Id
    private String id;
    private ApplicationStatus status;
    private ApplicantDTO applicant;
    private JobListingDTO job;
    @CreatedDate
    private Date createdAt;
}
