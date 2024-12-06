package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.KpiCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiKpiRepository extends JpaRepository<Kpi, Long>, KpiCommandRepository {
}
