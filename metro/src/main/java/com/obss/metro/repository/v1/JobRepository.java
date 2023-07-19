package com.obss.metro.repository.v1;

import com.obss.metro.entity.v1.Job;
import com.obss.metro.service.v1.JobService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    /**
     *
     * @return Set of uncategorized jobs
     * @see Job
     */
    Set<Job> findAllBy();

    /**
     * Sets Job status as {@link Job.Status#REMOVED}
     * @param id ID of the Job which is going to get removed
     * @see Job
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Job as j set j.status=\"REMOVED\" where j.status=\"ACTIVE\" and j.id=:id")
    void removeJobById(@Param("id") final Long id);

    /**
     *
     * Updates {@link Job.Status#ACTIVE}, expired Jobs by updating their status as {@link Job.Status#CLOSED}
     * @see JobService#updateExpired()
     * @see Job
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Job as j set j.status=\"CLOSED\" where j.dueDate < current_timestamp and j.status=\"ACTIVE\"")
    void findAndUpdateActiveExpired(); // todo: return updated ids to log
}
