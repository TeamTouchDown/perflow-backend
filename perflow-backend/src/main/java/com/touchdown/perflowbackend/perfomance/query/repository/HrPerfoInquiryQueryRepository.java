package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoInquiry;
import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoInquiryResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HrPerfoInquiryQueryRepository extends JpaRepository<HrPerfoInquiry, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoInquiryResponseDTO(" +
            "hph.perfoed_emp.empId, e.name, d.name, p.name, j.name, hpi.reason, hph.adjustmentColScore, hph.adjustmentDownScore, hph.perfo_emp.name " +
            ") " +
            "FROM Employee e " +
            "JOIN e.dept d " +
            "JOIN e.position p " +
            "JOIN e.job j " +
            "JOIN HrPerfo h ON e.empId = h.emp.empId " +
            "JOIN HrPerfoHistory hph ON hph.perfoed_emp.empId = e.empId AND FUNCTION('YEAR', hph.createDatetime) = :currentYear " +
            "JOIN HrPerfoInquiry hpi On hpi.hrPerfo.hrPerfoId = h.hrPerfoId " +
            "WHERE d.departmentId = :deptId ")
    List<HrPerfoInquiryResponseDTO> findbydeptId(Long deptId, Integer currentYear);
}
