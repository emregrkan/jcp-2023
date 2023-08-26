package org.foundation.atomjoblistingservice.entity.attributes;

import lombok.Data;
import org.foundation.atomjoblistingservice.dto.ApplicantResponseDTO;

import java.util.Date;

@Data
public class Application {
    private String id;
    private String status;
    private ApplicantResponseDTO applicant;
    private Date createdAt;
}
