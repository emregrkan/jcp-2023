package org.foundation.atomjoblistingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.foundation.atomjoblistingservice.constant.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobListing {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String title;
    private String about;
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    @Enumerated(EnumType.STRING)
    private PositionType positionType;
    @Enumerated(EnumType.STRING)
    private WorkplaceType workplaceType;
    @Enumerated(EnumType.STRING)
    private Location location;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Timestamp activationDate;
    private Timestamp dueDate;
    @CreationTimestamp
    private Timestamp createdAt;
    private boolean removed = false;
}

