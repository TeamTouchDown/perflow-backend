package com.touchdown.perflowbackend.employee.query.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
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

    // 입사년도 조회
    @Query("SELECT YEAR(e.joinDate) FROM Employee e WHERE e.empId = :empId")
    int findHireYearByEmpId(@Param("empId") String empId);

    // 직원의 부서 ID 조회
    @Query("SELECT e.dept.departmentId FROM Employee e WHERE e.empId = :loggedInEmpId")
    Long findDeptIdByEmpId(@Param("loggedInEmpId") String loggedInEmpId);


   /* // 직원의 부서 ID 조회
    @Query("SELECT e.dept.departmentId FROM Employee e WHERE e.empId = :loggedInEmpId")
    Long findDeptIdByEmpId(@Param("loggedInEmpId") String loggedInEmpId);


    // 직원이 팀장인지 확인
    @Query("SELECT CASE WHEN d.manageDeptId = e.dept.departmentId THEN true ELSE false END " +
            "FROM Employee e JOIN e.dept d WHERE e.empId = :loggedInEmpId")
    boolean isLeaderByEmpId(@Param("loggedInEmpId") String loggedInEmpId);


    // 직원이 인사팀인지 확인
    @Query("SELECT CASE WHEN e.dept.departmentId = 1 THEN true ELSE false END " +
            "FROM Employee e WHERE e.empId = :loggedInEmpId")
    boolean isHRByEmpId(@Param("loggedInEmpId") String loggedInEmpId);*/


}
