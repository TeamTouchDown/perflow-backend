package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiLimit;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.KpiLimitCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiLimitRepository extends JpaRepository<KpiLimit, Long>, KpiLimitCommandRepository {
}
