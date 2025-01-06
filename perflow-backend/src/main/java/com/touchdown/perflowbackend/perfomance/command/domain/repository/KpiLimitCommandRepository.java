package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiLimitCommandRepository extends JpaRepository<KpiLimit, Long> {
}
