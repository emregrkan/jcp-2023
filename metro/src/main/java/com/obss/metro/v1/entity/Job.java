package com.obss.metro.v1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

// todo: validation
// todo: hr user??
@Entity
@Where(clause = "status != 'REMOVED'")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder // dev only
public class Job {
  @Id
  @GeneratedValue
  private UUID id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "department_id")
  public Department department;

  @NotNull private String title;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Job.WorkplaceType workplaceType;

  // todo: find a better impl with validation; maps api?
  @NotNull private String location;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Job.Type type;

  @Basic(fetch = FetchType.LAZY)
  @Column(columnDefinition = "text")
  private String details;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Job.Status status;

  @CreationTimestamp
  @Column(updatable = false)
  private Timestamp postedAt;

  @UpdateTimestamp private Timestamp updatedAt;

  @Future(message = "Activation date must be in future")
  private Timestamp activationDate;

  @NotNull
  @Future(message = "Due date must be in future")
  private Timestamp dueDate;

  @OneToMany(mappedBy = "jobApplied", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<JobApplication> applications;

  public enum WorkplaceType {
    ON_SITE,
    HYBRID,
    REMOTE
  }

  public enum Type {
    FULL_TIME,
    PART_TIME,
    CONTRACT,
    TEMPORARY,
    VOLUNTEER,
    INTERNSHIP,
    OTHER
  }

  public enum Status {
    ACTIVE,
    CLOSED,
    REMOVED
  }
}
