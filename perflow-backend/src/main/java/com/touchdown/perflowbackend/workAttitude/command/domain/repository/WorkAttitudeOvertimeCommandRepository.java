package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;

import java.util.Optional;

public interface WorkAttitudeOvertimeCommandRepository {
    Overtime save(Overtime overtime);

    Optional<Overtime> findById(Long overtimeId);
}
