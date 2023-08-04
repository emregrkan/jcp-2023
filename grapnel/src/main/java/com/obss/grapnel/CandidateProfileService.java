package com.obss.grapnel;

import com.obss.grapnel.configuration.HtmlUnitConfiguration;
import com.obss.grapnel.dto.ProfileRequestDTO;
import com.obss.grapnel.dto.ProfileResponseDTO;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

  private static String tcn(final DomNode node) {
    return node != null ? node.getTextContent().trim() : null;
  }

  private static String tc(final DomNode node) {
    return node.getTextContent().trim();
  }

  public ProfileResponseDTO fetchCandidateProfile(final ProfileRequestDTO requestDTO)
      throws IOException {
    final WebClient webClient = htmlUnit.newWebClient();
    final HtmlPage page = webClient.getPage(requestDTO.url());
    final Pattern durationPattern =
        Pattern.compile("[A-Za-z]{3}\\s\\d{4}\\s-\\s(?:[A-Za-z]{3}\\s\\d{4}|Present)");

    final DomNode headlineNode = page.querySelector(".top-card-layout__headline");
    final DomNode locationNode = page.querySelector(".top-card-layout__first-subline");
    final DomNode aboutNode =
        page.querySelector("div.core-section-container__content p:not([class])");

    String location = tcn(locationNode);
    location = location != null ? location.split("\\n")[0] : null;

    final var experience =
        page.querySelector(".experience__list").getChildNodes().stream()
            .map(
                node -> {
                  if (node == null) return null;

                  final HtmlAnchor anchor =
                      node.querySelector(".profile-section-card__subtitle-link");
                  final DomNode titleNode = node.querySelector(".profile-section-card__title");
                  final DomNode jobLocationNode = node.querySelector(".experience-item__location");
                  final DomNode durationNode =
                      node.querySelector(".experience-item__duration span");

                  String companyName = null, companyPage = null, duration = null;

                  if (anchor != null) {
                    companyPage = anchor.getHrefAttribute().trim().split("\\?")[0];
                    companyName = anchor.getTextContent().trim();
                  }

                  if (durationNode != null) {
                    final Matcher matcher = durationPattern.matcher(tc(durationNode));
                    while (matcher.find()) duration = matcher.group();
                  }

                  return new ProfileResponseDTO.CandidateExperience(
                      tcn(titleNode), companyName, companyPage, tcn(jobLocationNode), duration);
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

                  final String degreeField = tcn(degreeNode);
                  String degree = null, field = null, duration = tcn(durationNode);

                  if (degreeField != null) {
                    final String[] split = degreeField.split("(?<=\\p{Ll})(?=\\p{Lu})");
                    if (split.length == 2) {
                      degree = split[0];
                      field = split[1];
                    } else {
                      degree = degreeField;
                    }
                  }

                  if (durationNode != null) {
                    final Matcher matcher = durationPattern.matcher(tc(durationNode));
                    while (matcher.find()) duration = matcher.group();
                  }

                  return new ProfileResponseDTO.CandidateEducation(
                      tcn(schoolNode), field, degree, duration);
                })
            .filter(edu -> edu != null && edu.school() != null)
            .toList();

    webClient.close();

    return new ProfileResponseDTO(
        requestDTO.id(),
        requestDTO.url(),
        tcn(headlineNode),
        location,
        tcn(aboutNode),
        experience,
        education);
  }
}
