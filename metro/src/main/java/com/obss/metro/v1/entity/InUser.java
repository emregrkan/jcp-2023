package com.obss.metro.v1.entity;

// todo: is this necessary?

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InUser {
  @Id private Long id;

  @NotNull
  @Column(unique = true)
  String inId;

  @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<JobApplication> applications;

  public void addJobApplication(final JobApplication jobApplication) {
    if (applications == null) applications = new HashSet<>();
    applications.add(jobApplication);
  }
}
