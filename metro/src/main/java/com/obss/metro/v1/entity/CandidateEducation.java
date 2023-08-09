package com.obss.metro.v1.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded;

@Entity
@Indexed
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidateEducation {
  @Id @GeneratedValue private UUID id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinColumn(name = "candidate_id")
  private Candidate candidate;

  @FullTextField
  private String school;
  @FullTextField
  private String field;
  private String degree;
  private String duration;
}
