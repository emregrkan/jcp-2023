package org.foundation.atomjoblistingservice.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ExperienceLevel {
    INTERNSHIP("Internship"),
    ENTRY_LEVEL("Entry Level"),
    ASSOCIATE("Associate"),
    MID_SENIOR_LEVEL("Mid-senior Level"),
    DIRECTOR("Director"),
    EXECUTIVE("Executive");

    final String level;

    @Override
    public String toString() {
        return level;
    }
}
