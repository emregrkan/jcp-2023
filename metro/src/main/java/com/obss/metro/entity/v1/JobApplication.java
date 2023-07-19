package com.obss.metro.entity.v1;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JobApplication {
    @Id
    public Long id;

    @NotNull
    @ManyToOne
    public InUser applicant;

    @NotNull
    @ManyToOne
    public Job appliedJob;

    @NotNull
    @Enumerated(EnumType.STRING)
    public JobApplication.Status status;

    public enum Status {
        SUBMITTED,
        VIEWED
    }
}
