package org.foundation.atomapplicationservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.foundation.atomapplicationservice.entity.attributes.*;

@Data
public class JobListingDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String title;
    @NotNull
    private PositionType positionType;
    @NotNull
    private java.util.Date createdAt;
}
