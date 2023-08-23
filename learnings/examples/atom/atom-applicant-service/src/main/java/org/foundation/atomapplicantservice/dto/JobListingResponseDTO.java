package org.foundation.atomapplicantservice.dto;

import lombok.Data;

@Data
public class JobListingResponseDTO {
    private String id;
    private String title;
    private String positionType;
    private java.util.Date createdAt;
}
