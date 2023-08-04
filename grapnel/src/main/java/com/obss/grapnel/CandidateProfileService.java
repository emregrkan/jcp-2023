package com.obss.grapnel;

import com.obss.grapnel.configuration.HtmlUnitConfiguration;
import com.obss.grapnel.dto.ProfileRequestDTO;
import com.obss.grapnel.dto.ProfileResponseDTO;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CandidateProfileService {
  private final HtmlUnitConfiguration htmlUnit;

  private static String tc(final DomNode node) {
    return node != null ? node.getTextContent().trim() : null;
  }

  public ProfileResponseDTO fetchCandidateProfile(final ProfileRequestDTO requestDTO)
      throws IOException {
    final WebClient webClient = htmlUnit.newWebClient();
    final HtmlPage page = webClient.getPage(requestDTO.url());

    final DomNode headlineNode = page.querySelector(".top-card-layout__headline");
    final DomNode locationNode = page.querySelector(".top-card-layout__first-subline");
    final DomNode aboutNode =
        page.querySelector("div.core-section-container__content p:not([class])");
    final var experience =
        page.querySelector(".experience__list").getChildNodes().stream()
            .map(
                node -> {
                  if (node == null) return null;
                  final HtmlAnchor anchor =
                      node.querySelector(".profile-section-card__subtitle-link");
                  String companyName = null, companyPage = null;

                  if (anchor != null) {
                    companyPage = anchor.getHrefAttribute().trim().split("\\?")[0];
                    companyName = anchor.getTextContent().trim();
                  }

                  final DomNode titleNode = node.querySelector(".profile-section-card__title");
                  final DomNode jobLocationNode = node.querySelector(".experience-item__location");
                  final DomNode durationNode =
                      node.querySelector(".experience-item__duration span");

                  return new ProfileResponseDTO.CandidateExperience(
                      tc(titleNode),
                      companyName,
                      companyPage,
                      tc(jobLocationNode),
                      tc(durationNode));
                })
            .filter(exp -> exp != null && exp.title() != null)
            .toList();

    final var education =
        page.querySelector(".education__list").getChildNodes().stream()
            .map(
                node -> {
                  if (node == null) return null;

                  final DomNode schoolNode =
                      node.querySelector(".profile-section-card__title-link");
                  final DomNode degreeNode = node.querySelector(".profile-section-card__subtitle");
                  final DomNode durationNode = node.querySelector(".education__item--duration");

                  String degree = null, field = null;
                  String degreeField = tc(degreeNode);

                  if (degreeField != null) {
                    String[] split = degreeField.split("(?<=\\p{Ll})(?=\\p{Lu})");
                    if (split.length == 2) {
                      degree = split[0];
                      field = split[1];
                    } else {
                        degree = degreeField;
                    }
                  }

                  return new ProfileResponseDTO.CandidateEducation(
                      tc(schoolNode), field, degree, tc(durationNode));
                })
            .filter(edu -> edu != null && edu.school() != null)
            .toList();

    webClient.close();

    return new ProfileResponseDTO(
        requestDTO.id(),
        requestDTO.url(),
        tc(headlineNode),
        tc(locationNode),
        tc(aboutNode),
        experience,
        education);
  }
}
