package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkAttitudeAnnualQueryRepository extends JpaRepository<Annual, Long> {

    // 특정 직원의 연차 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO(" +
            "a.annualId, a.empId.empId, a.empId.name, a.approver.empId, a.approver.name, " +
            "a.enrollAnnual, a.annualStart, a.annualEnd, a.annualType, a.status, " +
            "a.annualRejectReason, a.isAnnualRetroactive, a.annualRetroactiveReason, a.annualRetroactiveStatus) " +
            "FROM Annual a WHERE a.empId.empId = :empId AND a.status != 'DELETED'")
    List<WorkAttitudeAnnualResponseDTO> findByEmpId(@Param("empId") String empId);

    // 특정 부서의 연차 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO(" +
            "a.annualId, a.empId.empId, a.empId.name, a.approver.empId, a.approver.name, " +
            "a.enrollAnnual, a.annualStart, a.annualEnd, a.annualType, a.status, " +
            "a.annualRejectReason, a.isAnnualRetroactive, a.annualRetroactiveReason, a.annualRetroactiveStatus) " +
            "FROM Annual a WHERE a.empId.dept.departmentId = :deptId AND a.status != 'DELETED'")
    List<WorkAttitudeAnnualResponseDTO> findByDepartment(@Param("deptId") Long deptId);

    // 모든 연차 내역 조회 (인사팀)
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO(" +
            "a.annualId, a.empId.empId, a.empId.name, a.approver.empId, a.approver.name, " +
            "a.enrollAnnual, a.annualStart, a.annualEnd, a.annualType, a.status, " +
            "a.annualRejectReason, a.isAnnualRetroactive, a.annualRetroactiveReason, a.annualRetroactiveStatus) " +
            "FROM Annual a WHERE a.status != 'DELETED'")
    List<WorkAttitudeAnnualResponseDTO> findAllAnnuals();

    // 특정 직원의 사용한 연차 개수 조회
    @Query("SELECT a FROM Annual a " +
            "WHERE a.empId.empId = :empId " +
            "AND a.status = 'CONFIRMED' " +
            "AND a.annualEnd <= CURRENT_DATE")
    List<Annual> findUsedAnnuals(@Param("empId") String empId);


    @Query(value = "SELECT SUM(CASE " +
            "WHEN a.annual_status = 'MORNINGHALF' OR a.annual_status = 'AFTERNOONHALF' THEN 0.5 " +
            "ELSE DATEDIFF(a.annual_end, a.annual_start) + 1 END) " +
            "FROM annual a " +
            "WHERE a.emp_id = :empId " +
            "AND a.status = 'CONFIRMED' " + // 여기서는 승인된 연차만 체크
            "AND a.annual_end <= CURRENT_DATE", nativeQuery = true)
    Double countUsedAnnualDays(@Param("empId") String empId);

    @Query("SELECT a FROM Annual a " +
            "WHERE a.empId.empId = :empId " +
            "AND a.status = 'CONFIRMED' " +
            "AND a.annualEnd <= CURRENT_DATE") // 오늘까지 종료된 연차만
    List<Annual> findByEmpIdAndStatus(
            @Param("empId") String empId,
            @Param("status") Status status
    );

    @Query("SELECT a FROM Annual a " +
            "WHERE a.empId.empId = :empId " +
            "AND a.status = 'CONFIRMED' " +                // 승인된 연차
            "AND YEAR(a.annualStart) = :year " +           // 올해 연차만
            "AND a.annualEnd <= CURRENT_DATE")             // 종료일 기준 오늘 포함 이전
    List<Annual> findConfirmedAnnualsByYearAndEndDate(
            @Param("empId") String empId,
            @Param("year") int year
    );





}
