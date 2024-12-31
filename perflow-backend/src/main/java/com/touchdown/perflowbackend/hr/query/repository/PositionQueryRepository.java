package com.touchdown.perflowbackend.hr.query.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PositionQueryRepository extends JpaRepository<Position, Long> {

    @Query("SELECT e.position.positionLevel FROM Employee e WHERE e.empId = :empId")
    Integer findPositionLevelByEmpId(@Param("empId") String empId);
}
