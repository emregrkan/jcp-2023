package com.obss.grapnel.model;

public record Experience(
        String title,
        Company company,
        String location,
        String duration
) {
}
