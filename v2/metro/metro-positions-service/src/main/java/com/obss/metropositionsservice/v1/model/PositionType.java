package com.obss.metropositionsservice.v1.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PositionType {
    FULL_TIME("Full-time"),
    PART_TIME("Part-time"),
    CONTRACT("Contract"),
    TEMPORARY("Temporary"),
    VOLUNTEER("Volunteer"),
    INTERNSHIP("Internship"),
    OTHER("Other");

    final String positionType;
}
