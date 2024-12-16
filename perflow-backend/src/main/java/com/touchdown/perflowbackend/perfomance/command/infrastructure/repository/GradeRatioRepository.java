package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.GradeRatio;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.GradeCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRatioRepository extends JpaRepository<GradeRatio, Long> , GradeCommandRepository {
}
