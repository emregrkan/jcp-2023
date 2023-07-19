package com.obss.metro.repository.v1;

import com.obss.metro.entity.v1.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

// todo: check if this is needed

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Set<JobApplication> findAllByAppliedJobId(final Long id);
}
