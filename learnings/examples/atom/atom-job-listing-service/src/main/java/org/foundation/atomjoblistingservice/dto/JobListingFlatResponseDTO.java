package org.foundation.atomjoblistingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListingFlatResponseDTO {
    private String id;
    private String title;
    private String positionType;
    private java.util.Date createdAt;
}
