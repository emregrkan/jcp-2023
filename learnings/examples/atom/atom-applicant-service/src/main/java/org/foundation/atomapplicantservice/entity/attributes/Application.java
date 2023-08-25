package org.foundation.atomapplicantservice.entity.attributes;

import lombok.Data;
import org.foundation.atomapplicantservice.dto.JobListingResponseDTO;

import java.util.Date;

@Data
public class Application {
    private String id;
    private String status;
    private JobListingResponseDTO job;
    private Date createdAt;
}
