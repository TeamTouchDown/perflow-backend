package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.AiPerfoSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiPerfoSummaryCommandRepository extends JpaRepository<AiPerfoSummary, Long> {
}
