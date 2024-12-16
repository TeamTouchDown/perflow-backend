package com.touchdown.perflowbackend.hr.query.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionQueryRepository extends JpaRepository<Position, Long> {
}
