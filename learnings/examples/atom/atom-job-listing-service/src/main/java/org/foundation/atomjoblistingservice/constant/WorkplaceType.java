package org.foundation.atomjoblistingservice.constant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WorkplaceType {
    ON_SITE("On-site"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    final String workspaceType;

    @Override
    public String toString() {
        return workspaceType;
    }
}
