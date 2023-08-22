package org.foundation.atomapplicationservice.dto;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.foundation.atomapplicationservice.entity.attributes.*;

@Data
public class JobListingDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String title;
    @NotBlank
    @Pattern(regexp = "Full-time|Part-time|Contract|Temporary|Volunteer|Internship|Other")
    private String positionType;
    @NotNull
    private java.util.Date createdAt;
}
