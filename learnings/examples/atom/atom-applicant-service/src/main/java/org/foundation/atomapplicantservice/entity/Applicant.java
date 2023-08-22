package org.foundation.atomapplicantservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
}
