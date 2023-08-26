package org.foundation.atomapplicantservice.entity;

import lombok.Data;
import org.foundation.atomapplicantservice.entity.attributes.Application;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Document
@CompoundIndex(def = "{'applications.job.id': 1}")
public class Applicant {
    @Id
    private UUID id;
    private String url;
    @Indexed(unique = true)
    private String email;
    private String firstName;
    private String lastName;
    private String picture;
    private String blacklistReason;
    private Set<Application> applications = new HashSet<>();
}
