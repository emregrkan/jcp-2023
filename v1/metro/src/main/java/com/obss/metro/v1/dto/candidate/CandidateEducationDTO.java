package com.obss.metro.v1.dto.candidate;

import com.obss.metro.v1.entity.CandidateEducation;

public record CandidateEducationDTO(
        String school,
        String field,
        String degree,
        String duration
) {
    public static CandidateEducationDTO fromCandidateEducation(final CandidateEducation education) {
        return new CandidateEducationDTO(education.getSchool(), education.getField(), education.getDegree(), education.getDuration());
    }
}
