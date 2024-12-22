package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAnnualRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;

import java.util.Optional;

public interface WorkAttitudeAnnualCommandRepository {

    Optional<Annual> findById(Long annualId);

    Annual save(Annual annual);

}
