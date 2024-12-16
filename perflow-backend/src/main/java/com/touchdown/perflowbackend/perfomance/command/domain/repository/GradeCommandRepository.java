package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.GradeRatio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeCommandRepository extends JpaRepository<GradeRatio, Long> {
}
