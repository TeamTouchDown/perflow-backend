package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;

import java.util.Optional;

public interface PositionCommandRepository {

    Optional<Position> findById(Long positionId);

    Position save(Position position);

    void delete(Position position);
}
