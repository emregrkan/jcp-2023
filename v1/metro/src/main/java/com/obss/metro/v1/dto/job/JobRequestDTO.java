package com.obss.metro.v1.dto.job;

import com.obss.metro.v1.entity.Job;
import java.sql.Timestamp;
import java.util.UUID;

// todo: fix all confusions
public record JobRequestDTO(
    String title,
    UUID departmentId,
    Job.WorkplaceType workplaceType,
    String location,
    Job.Type type,
    Job.Status status,
    Timestamp dueDate) {
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
