package org.foundation.atomjoblistingservice.dto;

import lombok.Data;

@Data
public class JobListingFlatResponseDTO {
    private String id;
    private String title;
    private String positionType;
    private java.util.Date createdAt;
}
