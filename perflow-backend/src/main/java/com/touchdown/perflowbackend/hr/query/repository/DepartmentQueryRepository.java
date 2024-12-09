package com.touchdown.perflowbackend.hr.query.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentQueryRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d")
    Optional<List<Department>> findAllDepts();

    List<Department> findByNameContaining(String name);

    @Query("SELECT d FROM Department d WHERE d.manageDept.departmentId = :manageDeptId")
    List<Department> findByManageDeptId(@Param("manageDeptId") Long manageDeptId);
}
