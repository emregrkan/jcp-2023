package com.obss.metropositionsservice.v1.model;

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
}
