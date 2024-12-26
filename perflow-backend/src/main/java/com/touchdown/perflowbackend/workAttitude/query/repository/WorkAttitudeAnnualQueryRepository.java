package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkAttitudeAnnualQueryRepository extends JpaRepository<Annual, Long> {

    // 직원: 연차 사용 횟수 조회 (종류별)
    @Query("SELECT COUNT(a) FROM Annual a WHERE a.empId.empId = :empId AND a.status = 'CONFIRMED'")
    int findAnnualUsageByEmpId(@Param("empId") String empId);

    // 직원: 사용된 연차 일수 계산
    @Query("SELECT SUM(CASE " +
            "WHEN a.annualType = 'FULLDAY' THEN 1 " +
            "WHEN a.annualType = 'MORNINGHALF' THEN 0.5 " +
            "WHEN a.annualType = 'AFTERNOONHALF' THEN 0.5 " +
            "ELSE 0 END) " +
            "FROM Annual a " +
            "WHERE a.empId.empId = :empId AND a.status = 'CONFIRMED'")
    Double countUsedAnnualDays(@Param("empId") String empId);

    // 직원: 연차 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO(" +
            "a.annualId, a.empId.empId, a.empId.name, a.approveSbjId.approveSbjId, a.enrollAnnual, " +
            "a.annualStart, a.annualEnd, a.annualType, a.status, a.annualRejectReason, " +
            "a.isAnnualRetroactive, a.annualRetroactiveReason, a.annualRetroactiveStatus, a.approveSbjId.sbjUser.name) " +
            "FROM Annual a " +
            "WHERE a.empId.empId = :empId")
    List<WorkAttitudeAnnualResponseDTO> findAnnualListByEmpId(@Param("empId") String empId);

    // 팀장: 부서 내 연차 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO(" +
            "a.annualId, a.empId.empId, a.empId.name, a.approveSbjId.approveSbjId, a.enrollAnnual, " +
            "a.annualStart, a.annualEnd, a.annualType, a.status, a.annualRejectReason, " +
            "a.isAnnualRetroactive, a.annualRetroactiveReason, a.annualRetroactiveStatus, a.approveSbjId.sbjUser.name) " +
            "FROM Annual a " +
            "WHERE a.empId.dept.departmentId = :deptId")
    List<WorkAttitudeAnnualResponseDTO> findAnnualListByDeptId(@Param("deptId") String deptId);

    // 인사팀: 전체 연차 내역 조회
    @Query("SELECT new com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO(" +
            "a.annualId, a.empId.empId, a.empId.name, a.approveSbjId.approveSbjId, a.enrollAnnual, " +
            "a.annualStart, a.annualEnd, a.annualType, a.status, a.annualRejectReason, " +
            "a.isAnnualRetroactive, a.annualRetroactiveReason, a.annualRetroactiveStatus, a.approveSbjId.sbjUser.name) " +
            "FROM Annual a")
    List<WorkAttitudeAnnualResponseDTO> findAllAnnualList();
}
