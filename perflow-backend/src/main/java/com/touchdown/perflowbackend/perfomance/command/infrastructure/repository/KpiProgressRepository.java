package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiProgressStatus;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.KpiProgressCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiProgressRepository extends JpaRepository<KpiProgressStatus, Long>, KpiProgressCommandRepository {
}
