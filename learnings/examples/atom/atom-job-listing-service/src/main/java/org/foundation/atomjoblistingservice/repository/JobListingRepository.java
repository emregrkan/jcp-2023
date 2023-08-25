package org.foundation.atomjoblistingservice.repository;

import jakarta.validation.constraints.NotNull;
import org.foundation.atomjoblistingservice.entity.JobListing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface JobListingRepository extends MongoRepository<JobListing, String> {
    @Query("{'applications.applicant._id': '?0' }")
    Set<JobListing> findJobListingByApplicantId(@NotNull final UUID id);
}
