package com.obss.metro.v1.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.*;
import org.hibernate.search.mapper.pojo.bridge.mapping.annotation.PropertyBinderRef;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.processing.TypeMapping;

@Entity
@Indexed
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {
  @Id @DocumentId private UUID id;
  @FullTextField private String firstName;
  @FullTextField private String lastName;
  @FullTextField private String email;
  private String profilePicture;
  @FullTextField private String headline;
  @FullTextField private String location;
  @FullTextField private String about;

  @OneToMany(
      mappedBy = "candidate",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @IndexedEmbedded
  private List<CandidateExperience> experience;

  @OneToMany(
      mappedBy = "candidate",
      cascade = CascadeType.ALL,
      fetch = FetchType.EAGER,
      orphanRemoval = true)
  @IndexedEmbedded
  private List<CandidateEducation> education;

  @Column(unique = true)
  @FullTextField
  private String inUrl;

  @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private Set<JobApplication> applications;
}
