package org.foundation.atomjoblistingservice.entity;

import lombok.Data;
import org.foundation.atomjoblistingservice.entity.attributes.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Document
public class JobListing {
    @Id
    private String id;
    private String title;
    private String about;
    private ExperienceLevel experienceLevel;
    private PositionType positionType;
    private WorkplaceType workplaceType;
    private Location location;
    private Status status;
    private java.util.Date activationDate;
    private java.util.Date dueDate;
    private Set<Application> applications = new HashSet<>();
    @CreatedDate
    private java.util.Date createdAt;
    private boolean removed = false;
}

