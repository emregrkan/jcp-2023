package org.foundation.atomapplicantservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.util.UUID;


@Data
public class ApplicantRequestDTO {
    private UUID id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @URL
    @NotBlank
    private String picture;
    private String blacklistReason;
}
