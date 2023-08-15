package com.obss.metro.v1.controller;

import com.obss.metro.v1.dto.department.DepartmentDTO;
import com.obss.metro.v1.entity.Department;
import com.obss.metro.v1.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping
    public Set<DepartmentDTO> getAllDepartments() {
        return departmentService.findAllDepartments();
    }
}
