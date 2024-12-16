package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkAttitudeAttendanceQueryRepository extends JpaRepository<Attendance, Long> {

    // 특정 사원의 기간별 출퇴근 기록 조회 (사원 단위로 기간을 기준으로 조회)
    @Query("SELECT a FROM Attendance a WHERE a.empId.empId = :empId AND a.checkInDateTime BETWEEN :startDate AND :endDate")
    List<Attendance> findByEmpIdAndPeriod(@Param("empId") String empId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 특정 부서에 속한 사원들의 기간별 출퇴근 기록 조회 (팀장 또는 인사팀용)
    @Query("SELECT a FROM Attendance a WHERE a.empId.dept.departmentId = :deptId AND a.checkInDateTime BETWEEN :startDate AND :endDate")
    List<Attendance> findByDeptIdAndPeriod(@Param("deptId") Long deptId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 전체 사원의 기간별 출퇴근 기록 조회 (인사팀용)
    @Query("SELECT a FROM Attendance a WHERE a.checkInDateTime BETWEEN :startDate AND :endDate")
    List<Attendance> findByPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // 사원의 특정 출퇴근 기록 조회 (사원 단위로 전체 출퇴근 기록 조회)
    @Query("SELECT a FROM Attendance a WHERE a.empId.empId = :empId")
    List<Attendance> findByEmpId(@Param("empId") String empId);

    // 팀원들의 출퇴근 기록 조회 (팀장이나 인사팀에서 여러 사원의 출퇴근 기록을 조회할 때 사용)
    @Query("SELECT a FROM Attendance a WHERE a.empId.empId IN :empIds")
    List<Attendance> findByEmpIds(@Param("empIds") List<String> empIds);

}
