package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiLimit;
import com.touchdown.perflowbackend.perfomance.query.dto.KPILimitDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KpiLimitQueryRepository extends JpaRepository<KpiLimit, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPILimitDTO(" +
            "l.personalKpiMin, l.personalKpiMax, l.teamKpiMin, l.teamKpiMax " +
            ") " +
            "FROM KpiLimit l " +
            "WHERE l.department.departmentId = :deptId " +
            "  AND l.createDatetime = (" +
            "      SELECT MAX(l2.createDatetime) " +
            "      FROM KpiLimit l2 " +
            "      WHERE l2.department.departmentId = :deptId" +
            "  )")
    KPILimitDTO findkpilimitbydeptId(@Param("deptId") Long deptId);
}
