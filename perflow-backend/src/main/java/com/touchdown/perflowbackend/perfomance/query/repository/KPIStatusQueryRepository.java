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
            "JOIN s.kpi k ON s.kpiPassId = :kpiId " +
            "JOIN k.emp e ON e.empId = :empId " +
            "WHERE s.createDatetime = (" +
            "   SELECT MAX(subS.createDatetime) " +
            "   FROM KpiStatus subS " +
            "   WHERE subS.kpiPassId = :kpiId" +
            ")")
            KPIRejectReponseDTO findrejectbyempIdandkpiId(String empId, Long kpiId);
}
