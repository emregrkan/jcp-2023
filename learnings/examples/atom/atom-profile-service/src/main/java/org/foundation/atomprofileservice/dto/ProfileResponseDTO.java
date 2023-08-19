package org.foundation.atomprofileservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ProfileResponseDTO {
    private UUID id;
    private String url;
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
}
