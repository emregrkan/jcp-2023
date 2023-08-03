package com.obss.grapnel;

import com.obss.grapnel.model.Company;
import com.obss.grapnel.model.Education;
import com.obss.grapnel.model.Experience;
import com.obss.grapnel.model.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.htmlunit.WebClient;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserProfileService {
    private final WebClient webClient;

    public Profile getUserProfile(final String url) throws IOException {
        final HtmlPage page = webClient.getPage(url);

        final DomNode headlineNode = page.querySelector(".top-card-layout__headline");
        final DomNode locationNode = page.querySelector(".top-card-layout__first-subline");
        final DomNode aboutNode = page.querySelector(".core-section-container__content p");

        final String headline = headlineNode != null ? headlineNode.getTextContent().trim() : null;
        final String location = locationNode != null ? locationNode.getTextContent().trim() : null;
        final String about = aboutNode != null ? aboutNode.getTextContent().trim() : null;

        final List<Experience> experience = page.querySelector(".experience__list").getChildNodes().parallelStream().map(node -> {
            if (node == null) return null;

            final HtmlAnchor anchor = node.querySelector(".profile-section-card__subtitle-link");
            Company company = null;

            if (anchor != null) {
                final String companyPage = anchor.getHrefAttribute().trim();
                final String companyName = anchor.getTextContent().trim();
                company = new Company(companyName, companyPage);
            }

            final DomNode titleNode = node.querySelector(".profile-section-card__title");
            final DomNode jobLocationNode = node.querySelector(".experience-item__location");
            final DomNode durationNode = node.querySelector(".experience-item__duration span");

            final String title = titleNode != null ? titleNode.getTextContent().trim() : null;
            final String jobLocation = jobLocationNode != null ? jobLocationNode.getTextContent().trim() : null;
            final String duration = (durationNode != null) ? durationNode.getTextContent().trim() : null;

            if (title == null && company == null && jobLocation == null && duration == null) return null;

            return new Experience(title, company, jobLocation, duration);
        }).filter(Objects::nonNull).toList();

        final List<Education> education = page.querySelector(".education__list").getChildNodes().parallelStream().map(node -> {
            if (node == null) return null;

            final DomNode schoolNode = node.querySelector(".profile-section-card__title-link");
            final DomNode degreeNode = node.querySelector(".profile-section-card__subtitle");
            final DomNode durationNode = node.querySelector(".education__item--duration");

            final String school = schoolNode != null ? schoolNode.getTextContent().trim() : null;
            final String degree = degreeNode != null ? degreeNode.getTextContent().trim() : null;
            final String duration = durationNode != null ? durationNode.getTextContent().trim() : null;

            if (school == null && degree == null && duration == null) return null;

            return new Education(school, degree, duration);
        }).filter(Objects::nonNull).toList();

        return new Profile(headline, location, about, experience, education);
    }
}
