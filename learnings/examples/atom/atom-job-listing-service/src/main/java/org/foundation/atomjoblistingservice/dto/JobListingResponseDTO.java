package org.foundation.atomjoblistingservice.dto;

import lombok.Data;
import org.foundation.atomjoblistingservice.entity.attributes.*;

@Data
public class JobListingResponseDTO {
    private String id;
    private String title;
    private String about;
    private String experienceLevel;
    private String positionType;
    private String workplaceType;
    private String location;
    private Status status;
    private java.util.Date createdAt;
}
