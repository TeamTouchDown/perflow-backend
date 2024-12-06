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
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIDetailResponseDTO(r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue) " +
            "FROM Kpi r JOIN Employee e ON e.empId = :empId WHERE (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.WAIT OR r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.APPROVAL) " +
             "AND r.personalType = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PersonalType.PERSONAL")
    List<KPIDetailResponseDTO> findPersonalKPIsByEmpId(String empId);

    // 사번을 통해 개인 KPI 제한 조회
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPILimitResponseDTO(l.personalKpiMin, l.personalKpiMax) " +
            "FROM KpiLimit l " +
            "JOIN l.department d " + // KpiLimit에서 Department 조인
            "JOIN Employee e ON d.departmentId = e.dept.departmentId " + // Employee와 Department 조인
            "WHERE e.empId = :empId") // userId 조건 추가
    Optional<KPILimitResponseDTO> findPersonalKPILimitByEmpId(String empId);
}
