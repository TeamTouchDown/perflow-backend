package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Vacation;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkAttitudeVacationCommandRepository {

    Vacation save(Vacation vacation);

    Optional<Vacation> findById(Long vacationId);

    boolean existsByEmpIdAndVacationStartBeforeAndVacationEndAfter(
            String empId, LocalDateTime end, LocalDateTime start);

}
