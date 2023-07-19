package com.obss.metro.dto;

import com.obss.metro.entity.v1.Job;
import lombok.NonNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;

// todo: fix @NonNull confusion
// todo: fix all confusions
public record JobDto(Long id, @NonNull String title, @NonNull Job.WorkplaceType workplaceType, @NonNull String location,
                     @NonNull Job.Type type, @NonNull Job.Status status, LocalDateTime postedAt,
                     LocalDateTime updatedAt, @NonNull LocalDateTime dueDate) {
    public static JobDto fromJob(final Job job) {
        return new JobDto(job.getId(), job.getTitle(), job.getWorkplaceType(), job.getLocation(), job.getType(), job.getStatus(), job.getPostedAt().toLocalDateTime(), job.getUpdatedAt().toLocalDateTime(), job.getDueDate().toLocalDateTime());
    }

    public Job toJob() {
        // todo: should i include auto-generated fields
        return Job.builder()
                .title(title)
                .workplaceType(workplaceType)
                .location(location)
                .type(type)
                .status(status)
                .dueDate(Timestamp.valueOf(dueDate))
                .build();
    }
}