package com.obss.metro.v1.dto.job;

import com.obss.metro.v1.entity.Job;
import java.sql.Timestamp;
import java.util.UUID;

// todo: fix confusion
// todo: fix all confusions
public record JobResponseDTO(
    UUID id,
    String title,
    String department,
    Job.WorkplaceType workplaceType,
    String location,
    Job.Type type,
    Job.Status status,
    Timestamp postedAt,
    Timestamp updatedAt,
    Timestamp dueDate) {
  public static JobResponseDTO fromJob(final Job job) {
    return new JobResponseDTO(
        job.getId(),
        job.getTitle(),
        job.getDepartment().getName(),
        job.getWorkplaceType(),
        job.getLocation(),
        job.getType(),
        job.getStatus(),
        job.getPostedAt(),
        job.getUpdatedAt(),
        job.getDueDate());
  }
}
