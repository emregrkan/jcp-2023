package com.obss.metro.v1.service;

import com.obss.metro.v1.dto.department.DepartmentDTO;
import com.obss.metro.v1.repository.DepartmentRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentService {
    public final DepartmentRepository departmentRepository;

    public Set<DepartmentDTO> findAllDepartments() {
        return departmentRepository.findAll().stream().map(DepartmentDTO::fromDepartment).collect(Collectors.toSet());
    }
}
