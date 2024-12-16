package com.touchdown.perflowbackend.perfomance.query.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.GradeRatio;
import com.touchdown.perflowbackend.perfomance.query.dto.RatioGradeResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GradeRatioQueryRepository extends JpaRepository<GradeRatio, Long> {

    @Query("SELECT new com.touchdown.perflowbackend.perfomance.query.dto.RatioGradeResponseDTO(" +
            "g.emp.name, g.sRatio, g.aRatio, g.bRatio, g.cRatio, g.dRatio, g.updateReason " +
            ") " +
            "FROM GradeRatio g " +
            "WHERE g.createDatetime = ( " +
            "       SELECT MAX(g2.createDatetime) " +
            "       FROM GradeRatio g2 " +
            "  )")
    RatioGradeResponseDTO findGradeRatio();
}
