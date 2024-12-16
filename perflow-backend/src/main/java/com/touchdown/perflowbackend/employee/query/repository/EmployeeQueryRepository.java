package com.touchdown.perflowbackend.employee.query.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeQueryRepository extends JpaRepository<Employee, String> {

    @Query("SELECT e FROM Employee e WHERE e.dept.departmentId = :departmentId")
    List<Employee> findByDeptId(@Param("deptId") Long departmentId);

    List<Employee> findAll(); // 사원 목록 조회

    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE'")
    List<Employee> findActiveEmployees();

    @Query("SELECT e FROM Employee e WHERE e.status = 'RESIGNED'")
    List<Employee> findResignedEmployees();

}
