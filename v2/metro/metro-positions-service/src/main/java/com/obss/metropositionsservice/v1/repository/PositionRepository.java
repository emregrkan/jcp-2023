package com.obss.metropositionsservice.v1.repository;

import com.obss.metropositionsservice.v1.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PositionRepository extends JpaRepository<Position, UUID> {
}
