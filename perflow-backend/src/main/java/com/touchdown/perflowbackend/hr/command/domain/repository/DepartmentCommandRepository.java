package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;

import java.util.Optional;

public interface DepartmentCommandRepository {

    Optional<Department> findById(Long departmentId);
}
