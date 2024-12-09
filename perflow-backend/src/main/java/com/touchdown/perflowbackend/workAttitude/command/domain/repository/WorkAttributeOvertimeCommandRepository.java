package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkAttributeOvertimeCommandRepository {
    Overtime save(Overtime overtime);

    Optional<Overtime> findById(Long overtimeId);
}
