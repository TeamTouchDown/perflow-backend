package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkAttitudeVacationQueryRepository extends JpaRepository<Vacation, Long> {

}
