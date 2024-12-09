package com.touchdown.perflowbackend.workAttitude.command.infrastructure.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttributeOvertimeCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkAttitudeOvertimeRepository extends WorkAttributeOvertimeCommandRepository, JpaRepository<Overtime, Long> {
}
