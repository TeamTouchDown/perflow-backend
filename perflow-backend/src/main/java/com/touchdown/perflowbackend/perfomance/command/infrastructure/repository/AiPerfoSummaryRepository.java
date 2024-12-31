package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.AiPerfoSummary;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.AiPerfoSummaryCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiPerfoSummaryRepository extends JpaRepository<AiPerfoSummary, Long> , AiPerfoSummaryCommandRepository {
}
