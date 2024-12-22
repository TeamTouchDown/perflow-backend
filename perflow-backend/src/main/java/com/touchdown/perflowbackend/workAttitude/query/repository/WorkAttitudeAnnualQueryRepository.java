package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkAttitudeAnnualQueryRepository extends JpaRepository<Annual, Long> {
}
