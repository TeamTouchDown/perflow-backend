package com.touchdown.perflowbackend.employee.query.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeQueryRepository extends JpaRepository<Employee, String> {

    List<Employee> findAll(); // 사원 목록 조회

    @Query("SELECT e FROM Employee e WHERE e.status = 'ACTIVE'")
    List<Employee> findActiveEmployees();

    @Query("SELECT e FROM Employee e WHERE e.status = 'RESIGNED'")
    List<Employee> findResignedEmployees();

    // 특정 부서에 속한 직원 조회
    List<Employee> findByDept_DepartmentId(Long deptId);

    @Query("SELECT e.dept.departmentId FROM Employee e WHERE e.empId = :leaderEmpId")
    Long findDeptIdByLeaderEmpId(@Param("leaderEmpId") String leaderEmpId);

    @Query("SELECT e FROM Employee e WHERE e.dept.departmentId = :deptId")
    List<Employee> findByDeptId(@Param("deptId") Long deptId);

    // 특정 부서에 속한 직원들의 `empId` 조회
    @Query("SELECT e.empId FROM Employee e WHERE e.dept.departmentId = :deptId")
    List<String> findEmpIdsByDeptId(@Param("deptId") Long deptId);

    // 이름으로 사원 검색
    Page<Employee> findByNameContaining(@Param("name") String name, Pageable pageable);

    Page<Employee> findEmployeeByStatus(Pageable pageable, EmployeeStatus employeeStatus);
}
