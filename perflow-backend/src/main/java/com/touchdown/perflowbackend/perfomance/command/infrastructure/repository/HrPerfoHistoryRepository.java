package com.touchdown.perflowbackend.perfomance.command.infrastructure.repository;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.HrPerfoHistory;
import com.touchdown.perflowbackend.perfomance.command.domain.repository.HrPerfoHistoryCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrPerfoHistoryRepository extends JpaRepository<HrPerfoHistory, Long>, HrPerfoHistoryCommandRepository {
}
