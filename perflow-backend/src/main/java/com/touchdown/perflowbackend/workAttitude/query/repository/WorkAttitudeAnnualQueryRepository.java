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
    @Query("SELECT COUNT(a) FROM Annual a " +
            "WHERE a.empId.empId = :empId " +
            "AND a.status = 'CONFIRMED' " +
            "AND a.annualEnd <= CURRENT_DATE")
    int countUsedAnnuals(@Param("empId") String empId);
}
