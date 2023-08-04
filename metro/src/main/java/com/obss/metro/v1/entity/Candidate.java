package com.obss.metro.v1.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {
  @Id private UUID id;
  private String firstName;
  private String lastName;
  private String email;
  private String profilePicture;
  private String headline;
  private String location;
  private String about;

  @OneToMany(
      mappedBy = "candidate",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<CandidateExperience> experience;

  @OneToMany(
      mappedBy = "candidate",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  private List<CandidateEducation> education;

  @Column(unique = true)
  private String inUrl;

  @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<JobApplication> applications;

  public void addJobApplication(final JobApplication jobApplication) {
    if (applications == null) applications = new HashSet<>();
    applications.add(jobApplication);
  }
}
