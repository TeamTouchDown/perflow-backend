package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiProgressStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiProgressCommandRepository extends JpaRepository<KpiProgressStatus, Long> {
}
