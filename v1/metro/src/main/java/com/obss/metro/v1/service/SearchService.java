package com.obss.metro.v1.service;

import com.obss.metro.v1.dto.candidate.CandidateAuthResponseDTO;
import com.obss.metro.v1.dto.jobapplication.JobApplicationListResponseDTO;
import com.obss.metro.v1.entity.Candidate;
import com.obss.metro.v1.entity.Job;
import com.obss.metro.v1.entity.JobApplication;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.hibernate.search.engine.search.common.BooleanOperator;
import org.hibernate.search.engine.search.predicate.dsl.MatchPredicateOptionsStep;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class SearchService {
  private final EntityManager entityManager;

  @Autowired
  public SearchService(final EntityManagerFactory entityManagerFactory) {
    entityManager = entityManagerFactory.createEntityManager();
  }

  @PostConstruct
  public void indexEntities() throws InterruptedException {
    final SearchSession session = Search.session(entityManager);
    final MassIndexer massIndexer = session.massIndexer(Candidate.class);

    massIndexer.startAndWait();
  }

  public Set<CandidateAuthResponseDTO> findCandidates(final Optional<String> query) {
    List<Candidate> candidates;
    final var session = Search.session(entityManager).search(Candidate.class);

    if (query.isPresent() && !query.get().isBlank()) {
      candidates =
          session
              .where(
                  candidate ->
                      candidate
                          .match()
                          .fields(
                              "firstName",
                              "lastName",
                              "email",
                              "headline",
                              "location",
                              "about",
                              "experience.title",
                              "experience.companyName",
                              "education.school",
                              "education.field")
                          .matching(query.get())
                          .fuzzy())
              .fetchAllHits();
    } else {
      candidates = session.where(SearchPredicateFactory::matchAll).fetchAllHits();
    }

    return candidates.stream()
        .map(CandidateAuthResponseDTO::fromCandidate)
        .collect(Collectors.toSet());
  }
}
