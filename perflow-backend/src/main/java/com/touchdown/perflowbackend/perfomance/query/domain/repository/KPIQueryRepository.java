package com.touchdown.perflowbackend.perfomance.query.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.perfomance.query.application.dto.KPIDetailResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KPIQueryRepository extends JpaRepository<Kpi, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.application.dto.KPIDetailResponseDTO(r.emp.empId, r.goal, r.goalValue,r.goalValueUnit,r.goalDetail, r.currentValue) " +
            "FROM Kpi r WHERE r.kpiId = :kpiId AND (r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.WAIT OR r.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus.APPROVAL) ")
    List<KPIDetailResponseDTO> findKPIsByKpiId(Long kpiId);

    @Query("SELECT new com.touchdown.perflowbackend.")
}
