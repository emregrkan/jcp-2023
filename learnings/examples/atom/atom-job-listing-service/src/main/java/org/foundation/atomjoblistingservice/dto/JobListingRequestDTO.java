package org.foundation.atomjoblistingservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.foundation.atomjoblistingservice.constant.*;


import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobListingRequestDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String about;
    @NotNull
    private ExperienceLevel experienceLevel;
    @NotNull
    private PositionType positionType;
    @NotNull
    private WorkplaceType workplaceType;
    @NotNull
    private Location location;
    @NotNull
    private Status status;
    @Future
    private Timestamp activationDate;
    @Future
    private Timestamp dueDate;
}
