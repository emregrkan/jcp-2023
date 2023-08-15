package com.obss.metropositionsservice.v1.controller;

import com.obss.metropositionsservice.v1.entity.Position;
import com.obss.metropositionsservice.v1.service.PositionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PositionController {
    private final PositionService positionService;

    @QueryMapping
    public Set<Position> allPositions() {
        return positionService.allPositions();
    }
}
