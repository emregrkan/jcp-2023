package org.foundation.atomapplicationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;

@Data
public class ApplicantDTO {
    @NotNull
    private UUID id;
    @URL
    @NotBlank
    private String url;
    @Email
    @NotBlank
    private String email;
    @URL
    @NotBlank
    private String picture;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
}
