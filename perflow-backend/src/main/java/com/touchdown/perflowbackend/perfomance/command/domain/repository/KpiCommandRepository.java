package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Kpi;

import java.util.Optional;

public interface KpiCommandRepository {

    Optional<Kpi> findByKpiId(Long kpiId);

    Kpi save(Kpi kpi);

    void deleteById(Long kpiId);
}
