package com.touchdown.perflowbackend.workAttitude.command.infrastructure.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Vacation;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeVacationCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkAttitudeVacationRepository extends WorkAttitudeVacationCommandRepository, JpaRepository<Vacation, Long> {
}
