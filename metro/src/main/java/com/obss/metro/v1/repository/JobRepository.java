package com.obss.metro.v1.repository;

import com.obss.metro.v1.entity.Job;
import com.obss.metro.v1.service.JobService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
  /**
   * Sets Job status as {@link Job.Status#REMOVED}
   *
   * @param id ID of the Job which is going to get removed
   * @see Job
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Job as j set j.status='REMOVED' where j.id=:id")
  void removeJobById(@Param("id") final Long id);

  /**
   * Updates {@link Job.Status#ACTIVE}, expired Jobs by updating their status as {@link
   * Job.Status#CLOSED}
   *
   * @see JobService#updateExpired()
   * @see Job
   * @author <a href="mailto:emre-gurkan@hotmail.com">Emre Gürkan</a>
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Job set status='CLOSED' where dueDate < current_timestamp and status='ACTIVE'")
  void findAndUpdateActiveExpired(); // todo: return updated ids to log

  /**
   *
   * @param id The job id
   * @return Optional of PosterId
   * @see Job#posterId
   */
  @Query("select j.posterId from Job as j where j.id = :id")
  Optional<UUID> findPosterIdById(@Param("id") final Long id);
}
