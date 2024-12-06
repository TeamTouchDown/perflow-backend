package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Perfo;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.PerfoCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaEvalutionRepository extends JpaRepository<Perfo, Long> , PerfoCommandRepository {
}
