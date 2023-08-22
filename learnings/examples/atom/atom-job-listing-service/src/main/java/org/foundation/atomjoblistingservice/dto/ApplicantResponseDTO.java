package org.foundation.atomjoblistingservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ApplicantResponseDTO {
    private String id;
    private String url;
    private String email;
    private String picture;
    private String firstName;
    private String lastName;
}
