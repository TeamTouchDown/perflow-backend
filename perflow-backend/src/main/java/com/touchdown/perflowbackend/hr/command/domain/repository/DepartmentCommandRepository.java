package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DepartmentCommandRepository {

    Optional<Department> findById(Long departmentId);

    Department save(Department newDepartment);

    @Query("SELECT d FROM Department d WHERE d.departmentId IN :departmentIds")
    List<Department> findAllById(@Param("departmentIds") Set<Long> departmentIds);
}
