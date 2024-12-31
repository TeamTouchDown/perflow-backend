package com.touchdown.perflowbackend.workAttitude.command.infrastructure.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeOvertimeCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkAttitudeOvertimeRepository extends WorkAttitudeOvertimeCommandRepository, JpaRepository<Overtime, Long> {
}
