package com.touchdown.perflowbackend.hr.command.infrastructure.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPositionCommandRepository extends JpaRepository<Position, Long>, PositionCommandRepository {
}
