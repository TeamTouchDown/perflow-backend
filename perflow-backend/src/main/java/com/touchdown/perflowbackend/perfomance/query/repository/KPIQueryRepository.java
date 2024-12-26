package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KPIQueryRepository extends JpaRepository<Kpi, Long> {

    // 사번을 통해 개인 KPI 목록 조회
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN Employee e ON e.empId = :empId WHERE (r.status != com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.EXPIRED AND r.status != com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.ACTIVE) " +
             "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.PERSONAL")
    List<KPIDetailResponseDTO> findPersonalKPIsByEmpId(String empId);

    // 사번을 통해 개인 KPI 기간별 목록 조회(현재 진행형, 년도만)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN r.emp e " +
            "WHERE r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.ACTIVE " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.PERSONAL " +
            "AND e.empId = :empId " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND SUBSTRING(r.period, 6) = 'YEAR'" )

    List<KPIDetailResponseDTO> findActivePersonalKPIsByEmpIdAndOnlyYear(
            String empId,
            String year
 );

    // 사번을 통해 개인 KPI 기간별 목록 조회(현재 진행형)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN r.emp e " +
            "WHERE r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.ACTIVE " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.PERSONAL " +
            "AND e.empId = :empId " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND (:quarter IS NULL OR SUBSTRING(r.period, 13) = :quarter ) " +
            "AND (:month IS NULL OR SUBSTRING(r.period, 11) = :month )")
    List<KPIDetailResponseDTO> findActivePersonalKPIsByEmpIdAndYear(
            String empId,
            String year,
            String quarter,
            String month);

    // 사번을 통해 개인 KPI 기간별 목록 조회(과거,년도만)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN Employee e ON e.empId = :empId " +
            "WHERE (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.EXPIRED) " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.PERSONAL " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND SUBSTRING(r.period, 6) = 'YEAR'" )
    List<KPIDetailResponseDTO> findExpiredPersonalKPIsByEmpIdAndOnlyYear(
            String empId,
            String year
    );


    // 사번을 통해 개인 KPI 기간별 목록 조회(과거)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN Employee e ON e.empId = :empId " +
            "WHERE (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.EXPIRED) " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.PERSONAL " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND (:quarter IS NULL OR SUBSTRING(r.period, 13) = :quarter ) " +
            "AND (:month IS NULL OR SUBSTRING(r.period, 11) = :month )")
    List<KPIDetailResponseDTO> findExpiredPersonalKPIsByEmpIdAndYear(
            String empId,
            String year,
            String quarter,
            String month);

    // 사번을 통해 개인 KPI 제한 조회
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO(l.personalKpiMin, l.personalKpiMax) " +
            "FROM KpiLimit l " +
            "JOIN l.department d " + // KpiLimit에서 Department 조인
            "JOIN Employee e ON d.departmentId = e.dept.departmentId " + // Employee와 Department 조인
            "WHERE e.empId = :empId") // userId 조건 추가
    Optional<KPILimitResponseDTO> findPersonalKPILimitByEmpId(String empId);

    // 사번을 통해 팀 KPI 목록 조회
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN Employee e ON e.empId = :empId WHERE (r.status != com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.EXPIRED AND r.status != com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.ACTIVE) " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.TEAM")
    List<KPIDetailResponseDTO> findTeamKPIsByEmpId(String empId);

    // 사번을 통해 팀 KPI 목록 조회 (현재 진행형,년도만)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN r.emp e " +
            "WHERE (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.ACTIVE) " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.TEAM " +
            "AND e.empId = :empId " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND SUBSTRING(r.period, 6) = 'YEAR'" )
    List<KPIDetailResponseDTO> findActiveTeamKPIsByEmpIdAndOnlyYear(
            String empId,
            String year
            );

    // 사번을 통해 팀 KPI 목록 조회 (현재 진행형,모두조회)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN r.emp e " +
            "WHERE (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.ACTIVE) " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.TEAM " +
            "AND e.empId = :empId " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND (:quarter IS NULL OR SUBSTRING(r.period, 13) = :quarter ) " +
            "AND (:month IS NULL OR SUBSTRING(r.period, 11) = :month )")
    List<KPIDetailResponseDTO> findActiveTeamKPIsByEmpIdAndYear(
            String empId,
            String year,
            String quarter,
            String month);

    // 사번을 통해 팀 KPI 목록 조회 (과거,년도만)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN Employee e ON e.empId = :empId " +
            "WHERE (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.EXPIRED) " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.TEAM " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND SUBSTRING(r.period, 6) = 'YEAR'" )
    List<KPIDetailResponseDTO> findExpiredTeamKPIsByEmpIdAndOnlyYear(
            String empId,
            String year
    );

    // 사번을 통해 팀 KPI 목록 조회 (과거,모두조회)
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.kpiId, r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue, r.status, r.personalType, r.period) " +
            "FROM Kpi r JOIN Employee e ON e.empId = :empId " +
            "WHERE (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.EXPIRED) " +
            "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.TEAM " +
            "AND SUBSTRING(r.period, 1, 4) = :year " +
            "AND (:quarter IS NULL OR SUBSTRING(r.period, 13) = :quarter ) " +
            "AND (:month IS NULL OR SUBSTRING(r.period, 11) = :month )")
    List<KPIDetailResponseDTO> findExpiredTeamKPIsByEmpIdAndYear(
            String empId,
            String year,
            String quarter,
            String month);

    // 사번을 통해 팀 KPI 제한 조회
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO(l.teamKpiMin, l.teamKpiMax) " +
            "FROM KpiLimit l " +
            "JOIN l.department d " + // KpiLimit에서 Department 조인
            "JOIN Employee e ON d.departmentId = e.dept.departmentId " + // Employee와 Department 조인
            "WHERE e.empId = :empId") // userId 조건 추가
    Optional<KPILimitResponseDTO> findTeamKPILimitByEmpId(String empId);
}
