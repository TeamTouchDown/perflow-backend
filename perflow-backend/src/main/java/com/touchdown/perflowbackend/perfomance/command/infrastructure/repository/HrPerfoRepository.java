package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfo;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrPerfoRepository extends JpaRepository<HrPerfo, Long> , HrPerfoCommandRepository {
}
