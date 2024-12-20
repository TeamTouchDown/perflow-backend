package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiStatus;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.KpiStatusCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiStatusRepository extends JpaRepository<KpiStatus, Long> , KpiStatusCommandRepository {
}
