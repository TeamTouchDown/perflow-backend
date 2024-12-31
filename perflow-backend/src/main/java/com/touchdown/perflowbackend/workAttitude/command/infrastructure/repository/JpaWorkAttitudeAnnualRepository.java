package com.touchdown.perflowbackend.workAttitude.command.infrastructure.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAnnualCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkAttitudeAnnualRepository extends WorkAttitudeAnnualCommandRepository, JpaRepository<Annual, Long> {
}
