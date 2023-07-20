package com.obss.metro.v1.repository;

import com.obss.metro.v1.entity.JobApplication;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// todo: check if this is needed

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
  Set<JobApplication> findAllByAppliedJobId(final Long id);
}
