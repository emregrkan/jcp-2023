package com.obss.metro.dto.v1.job;

import com.obss.metro.entity.v1.Job;

import java.sql.Timestamp;

// todo: fix all confusions
public record JobRequestDTO(String title, Job.WorkplaceType workplaceType, String location,
                            Job.Type type, Job.Status status, Timestamp dueDate) {
    public Job toJob() {
        return Job.builder()
                .title(title)
                .workplaceType(workplaceType)
                .location(location)
                .type(type)
                .status(status)
                .dueDate(dueDate)
                .build();
    }
}