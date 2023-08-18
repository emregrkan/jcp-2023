package org.foundation.atomjoblistingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListingFlatResponseDTO {
    private UUID id;
    private String title;
    private String positionType;
    private Timestamp createdAt;
}
