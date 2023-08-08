package com.obss.grapnel.dto;


import java.util.List;

public record ProfileResponseDTO(
    String id,
    String url,
    String headline,
    String location,
    String about,
    List<CandidateExperienceDTO> experience,
    List<CandidateEducationDTO> education) {
}
