package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.CandidateExperience;

public record CandidateExperienceDTO(
        String title,
        String companyName,
        String companyPage,
        String location,
        String duration
) {
    public static CandidateExperienceDTO fromCandidateExperience(final CandidateExperience experience) {
        return new CandidateExperienceDTO(experience.getTitle(), experience.getCompanyName(), experience.getCompanyPage(), experience.getLocation(), experience.getDuration());
    }
}
