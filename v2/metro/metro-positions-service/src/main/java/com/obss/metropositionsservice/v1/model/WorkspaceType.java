package com.obss.metropositionsservice.v1.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WorkspaceType {
    ON_SITE("On Site"),
    REMOTE("Remote"),
    HYBRID("Hybrid");

    final String workspaceType;
}
