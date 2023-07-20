package com.obss.metro.v1.entity;

// todo: is this necessary?

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InUser {
  @Id private String id;
  @OneToMany private Set<JobApplication> applications;
}
