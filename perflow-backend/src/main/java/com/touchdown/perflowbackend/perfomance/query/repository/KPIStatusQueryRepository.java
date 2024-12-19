package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiStatus;
import com.touchdown.perflowbackend.perfomance.query.dto.KPIRejectReponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface KPIStatusQueryRepository extends JpaRepository<KpiStatus, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.KPIRejectReponseDTO( " +
            "s.passReason" +
            ") " +
            "FROM KpiStatus s " +
            "JOIN s.kpi k " +
            "JOIN k.emp e " +
            "WHERE e.empId = :empId " +
            "AND k.kpiId = :kpiId " +
            "AND s.createDatetime = (" +
            "   SELECT MAX(subS.createDatetime) " +
            "   FROM KpiStatus subS " +
            "   WHERE subS.kpi.kpiId = :kpiId" +
            ")")
            KPIRejectReponseDTO findrejectbyempIdandkpiId(String empId, Long kpiId);
}
