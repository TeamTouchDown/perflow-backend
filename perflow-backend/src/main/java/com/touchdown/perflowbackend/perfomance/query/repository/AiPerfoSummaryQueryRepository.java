package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.AiPerfoSummary;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PerfoType;
import com.touchdown.perflowbackend.perfomance.query.dto.AiSummaryResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AiPerfoSummaryQueryRepository extends JpaRepository<AiPerfoSummary, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.AiSummaryResponseDTO( " +
            "a.aiSummary , a.perfoType " +
            ") " +
            "FROM AiPerfoSummary a " +
            "WHERE FUNCTION('YEAR', a.createDatetime) = :currentYear " +
            "AND a.perfoType = :type " +
            "AND a.emp.empId = :empId ")
    AiSummaryResponseDTO findAiPerfoSummaryByempIdandtype(
            @Param("currentYear") int currentYear,
            @Param("type") PerfoType type,
            @Param("empId") String empId
    );
}
