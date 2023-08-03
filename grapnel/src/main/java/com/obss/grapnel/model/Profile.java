package com.obss.grapnel.model;

import java.util.List;

public record Profile(
        String headline,
        String location,
        String about,
        List<Experience> experience,
        List<Education> education
) {
}
