package com.obss.metro.entity.v1;

// todo: is this necessary?

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InUser {
    @Id
    private String id;
    @OneToMany
    private Set<JobApplication> applications;
}
