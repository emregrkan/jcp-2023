package com.obss.metro.v1.repository;

import com.obss.metro.v1.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {}
