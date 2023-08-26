package org.foundation.atomapplicantservice.repository;

import org.foundation.atomapplicantservice.entity.Applicant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ApplicantRepository extends MongoRepository<Applicant, UUID> {
    @Query("{'applications.job.id': ObjectId('?0')}")
    Set<Applicant> findApplicantsByAppliedJobListingId(final String id);
}
