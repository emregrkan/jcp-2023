package com.obss.metro.v1.dto.department;

import com.obss.metro.v1.entity.Department;

import java.util.UUID;

public record DepartmentDTO(UUID id, String name) {
    public static DepartmentDTO fromDepartment(final Department department) {
        return new DepartmentDTO(department.getId(), department.getName());
    }
}
