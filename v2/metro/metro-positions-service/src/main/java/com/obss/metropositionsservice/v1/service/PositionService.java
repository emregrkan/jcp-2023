package com.obss.metropositionsservice.v1.service;

import com.obss.metropositionsservice.v1.entity.Position;
import com.obss.metropositionsservice.v1.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PositionService {
    private final PositionRepository positionRepository;

    public Set<Position> allPositions() {
        return new HashSet<>(positionRepository.findAll());
    }
}
