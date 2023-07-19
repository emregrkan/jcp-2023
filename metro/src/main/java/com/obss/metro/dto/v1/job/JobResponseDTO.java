package com.obss.metro.dto.v1.job;

import com.obss.metro.entity.v1.Job;

import java.sql.Timestamp;

// todo: fix confusion
// todo: fix all confusions
public record JobResponseDTO(
        Long id,
        String title,
        Job.WorkplaceType workplaceType,
        String location,
        Job.Type type,
        Job.Status status,
        Timestamp postedAt,
        Timestamp updatedAt,
        Timestamp dueDate
) {
    public static JobResponseDTO fromJob(final Job job) {
        return new JobResponseDTO(job.getId(), job.getTitle(), job.getWorkplaceType(), job.getLocation(), job.getType(), job.getStatus(), job.getPostedAt(), job.getUpdatedAt(), job.getDueDate());
    }
}