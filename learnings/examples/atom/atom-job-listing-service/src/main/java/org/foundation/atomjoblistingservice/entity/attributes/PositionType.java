package org.foundation.atomjoblistingservice.entity.attributes;

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

    @Override
    public String toString() {
        return positionType;
    }
}
