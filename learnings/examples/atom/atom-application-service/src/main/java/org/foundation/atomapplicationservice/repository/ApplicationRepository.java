package org.foundation.atomapplicationservice.repository;

import org.foundation.atomapplicationservice.entity.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
}
