package com.touchdown.perflowbackend.hr.query.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentQueryRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d")
    List<Department> findAllDepartments();
}
