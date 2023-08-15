package com.obss.metropositionsservice.v1.entity;

import com.obss.metropositionsservice.v1.model.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Position {
    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "text")
    private String about;
    @Enumerated(EnumType.STRING)
    private ExperienceLevel experienceLevel;
    @Enumerated(EnumType.STRING)
    private PositionType positionType;
    @Enumerated(EnumType.STRING)
    private WorkspaceType workspaceType;
    @Enumerated(EnumType.STRING)
    private Location location;
    @Enumerated(EnumType.STRING)
    private Status status;
    private Timestamp activationDate;
    private Timestamp dueDate;
    @CreationTimestamp
    private Timestamp createdAt;
}
