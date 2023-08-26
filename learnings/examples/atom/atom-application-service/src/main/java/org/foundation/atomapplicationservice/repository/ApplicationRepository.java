package org.foundation.atomapplicationservice.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.foundation.atomapplicationservice.entity.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    Set<Application> findApplicationsByApplicant_Id(@NotNull UUID applicant_id);
    Set<Application> findApplicationsByJob_Id(@NotBlank String job_id);
}
