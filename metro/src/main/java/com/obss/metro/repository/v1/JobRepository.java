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
     * @see com.obss.metro.entity.v1.Job
     */
    Set<Job> findAllBy();

    /**
     * Sets Job status as {@link Job.Status#REMOVED}
     * @param id ID of the Job which is going to get removed
     * @see Job
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Job as j set j.status=Job.Status.REMOVED where j.status=Job.Status.ACTIVE and j.id=:id")
    void removeJobById(@Param("id") final Long id);

    /**
     *
     * Updates the expired Jobs
     * @see JobService#updateExpired()
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Job as j set j.status=Job.Status.CLOSED where j.dueDate < current_timestamp and j.status=Job.Status.ACTIVE ")
    void findAndUpdateActiveExpired(); // todo: return updated ids to log
}
