package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Weight;
import com.touchdown.perflowbackend.perfomance.query.dto.RatioPerfoResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WeightQueryRepository extends JpaRepository<Weight, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.RatioPerfoResponseDTO( " +
            "w.dept.name, w.personalWeight, w.teamWeight, w.colWeight, w.downwardWeight, w.attendanceWeight, w.updateReason " +
            ") " +
            "FROM Weight w " +
            "WHERE w.dept.departmentId = :deptId " +
            "  AND w.createDatetime = ( " +
            "       SELECT MAX(w2.createDatetime) " +
            "       FROM Weight w2 " +
            "       WHERE w2.dept.departmentId = :deptId " +
            "  )")
    RatioPerfoResponseDTO findWeightByDeptId(Long deptId);
}
