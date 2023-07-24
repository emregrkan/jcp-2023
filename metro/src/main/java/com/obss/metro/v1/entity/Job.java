package com.obss.metro.v1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

// todo: validation
@Entity
@Where(clause = "status != 'REMOVED'")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder // dev only
public class Job {
  @Id private Long id;

  @NotNull private String title;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Job.WorkplaceType workplaceType;

  // todo: find a better impl with validation; maps api?
  @NotNull private String location;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Job.Type type;

  /*
   todo: probably content shouldn't be stored on postgres
    but instead elasticsearch to implement free format text search...
    I think fetched data will be merged on front end? maybe even Job
    should be on elasticsearch as well? I'm not sure...
  */
  private String details;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Job.Status status;

  @CreationTimestamp
  @Column(updatable = false)
  private Timestamp postedAt;

  @UpdateTimestamp private Timestamp updatedAt;

  @NotNull
  @Future(message = "Due date must be in future")
  private Timestamp dueDate;

  @OneToMany(mappedBy = "jobApplied", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<JobApplication> applications;

  // todo: replace this with snowflake
  @PrePersist
  private void setIdPrePersist() {
    if (this.id == null) this.id = ThreadLocalRandom.current().nextLong();
  }

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
