package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfo;
import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HrPerfoQueryRepository extends JpaRepository<HrPerfo, Long> {

    // 사번을 이용해 제한적인 인사 정보 조회하기
    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoResponseDTO(" +
            "CAST(p.grade AS string), p.review, CAST(FUNCTION('YEAR', p.createDatetime) AS Long)" +
            ") " +
            "FROM HrPerfo p " +
            "JOIN p.emp e " +
            "WHERE e.empId = :empId " +
            "AND p.status = com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoStatus.COMPLETE ")
    List<HrPerfoResponseDTO> findHrPerfoByEmpId(String empId);
}
