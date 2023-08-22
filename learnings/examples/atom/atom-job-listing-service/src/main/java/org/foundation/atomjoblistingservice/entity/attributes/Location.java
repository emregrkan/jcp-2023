package org.foundation.atomjoblistingservice.entity.attributes;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
public enum Location {
    ISTANBUL("Istanbul"),
    ANKARA("Ankara"),
    AMSTERDAM("Amsterdam"),
    LONDON("London");

    final String location;

    @Override
    public String toString() {
        return location;
    }
}
