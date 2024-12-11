package com.touchdown.perflowbackend.perfomance.command.domain.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrPerfoCommandRepository extends JpaRepository<HrPerfo, Long> {
}
