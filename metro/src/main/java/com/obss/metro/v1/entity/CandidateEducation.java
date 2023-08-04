package com.obss.metro.v1.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateEducation {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "candidate_id")
  private Candidate candidate;

  private String school;
  private String field;
  private String degree;
  private String duration;
}
