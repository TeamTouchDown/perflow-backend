package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoHistory;
import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoHistoryResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HrPerfoHistoryQueryRepository extends JpaRepository<HrPerfoHistory, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoHistoryResponseDTO( " +
            "p.hrPerfoHistoryId, p.perfo_emp.empId, p.perfoed_emp.empId, p.adjustmentDegree, p.adjustmentColScore, p.adjustmentDownScore, p.adjustmentReason " +
            ") " +
            "FROM HrPerfoHistory p " +
            "WHERE FUNCTION('YEAR', p.createDatetime) = :currentYear " +
            "AND p.perfo_emp.empId = :EmpId")
    List<HrPerfoHistoryResponseDTO> findHrPerfoHistoryByEmpId(
            String EmpId,
            int currentYear);
}
