package org.foundation.atomjoblistingservice.entity.attributes;

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
