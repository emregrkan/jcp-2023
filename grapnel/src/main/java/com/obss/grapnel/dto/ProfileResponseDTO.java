package com.obss.grapnel.dto;

import java.util.List;

public record ProfileResponseDTO(
    String id,
    String url,
    String headline,
    String location,
    String about,
    List<CandidateExperience> experience,
    List<CandidateEducation> education) {
  public record CandidateExperience(
      String title, String companyName, String companyPage, String location, String duration) {}

  public record CandidateEducation(String school, String field, String degree, String duration) {}
}
