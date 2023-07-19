package com.obss.metro.entity.v1;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


// todo: validation
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder // dev only
public class Job {
    @Id
    private Long id;

    @NotNull
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Job.WorkplaceType workplaceType;

    // todo: find a better impl with validation; maps api?
    @NotNull
    private String location;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Job.Type type;

    /*
     todo: probably content shouldn't be stored on postgres
      but instead on elasticsearch to implement free format text search...
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

    @UpdateTimestamp
    private Timestamp updatedAt;

    @NotNull
    @Future(message = "Due date must be in future")
    private Timestamp dueDate;

    @OneToMany
    private Set<JobApplication> applications;

    @PrePersist
    private void setIdPrePersist() {
        if (this.id == null) {
            this.id = ThreadLocalRandom.current().nextLong();
        }
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
