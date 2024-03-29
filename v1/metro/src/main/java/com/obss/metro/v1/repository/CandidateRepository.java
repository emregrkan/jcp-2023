package com.obss.metro.v1.repository;

import com.obss.metro.v1.entity.Candidate;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
}
