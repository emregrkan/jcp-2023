package com.obss.metropositionsservice.v1.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    SUBMITTED("Submitted"),
    VIEWED("Viewed"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");

    final String status;
}
