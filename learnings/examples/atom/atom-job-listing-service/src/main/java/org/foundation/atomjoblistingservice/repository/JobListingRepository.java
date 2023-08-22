package org.foundation.atomjoblistingservice.repository;

import org.foundation.atomjoblistingservice.entity.JobListing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JobListingRepository extends MongoRepository<JobListing, String> {
}
