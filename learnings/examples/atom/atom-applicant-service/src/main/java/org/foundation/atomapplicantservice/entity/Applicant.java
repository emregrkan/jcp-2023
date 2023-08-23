package org.foundation.atomapplicantservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.foundation.atomapplicantservice.dto.ApplicationResponseDTO;
import org.foundation.atomapplicantservice.entity.attributes.Application;
import org.foundation.atomapplicantservice.entity.attributes.Blacklist;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Applicant {
    @Id
    private UUID id;
    private String url;
    @Indexed(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
    private Blacklist blacklist;
    private Set<Application> applications = new HashSet<>();
}
