package com.obss.metropositionsservice.v1.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Location {
    ISTANBUL("Istanbul"),
    ANKARA("Ankara"),
    AMSTERDAM("Amsterdam"),
    LONDON("London");

    final String location;
}
