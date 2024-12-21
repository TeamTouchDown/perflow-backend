package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KpiStatusCommandRepository extends JpaRepository<KpiStatus, Long> {
}
