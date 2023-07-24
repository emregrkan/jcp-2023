package com.obss.metro.v1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobApplication {
  @Id private Long id;
  
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "applicant_id")
  public InUser applicant;
  
  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "jobApplied_id")
  public Job jobApplied;
  
  @NotNull
  @Enumerated(EnumType.STRING)
  public JobApplication.Status status;
  public enum Status {
    SUBMITTED,
    VIEWED
  }
}
