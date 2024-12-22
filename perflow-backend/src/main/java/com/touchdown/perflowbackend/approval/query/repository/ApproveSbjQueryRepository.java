package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApproveSbjQueryRepository extends JpaRepository<ApproveSbj, Long>, JpaSpecificationExecutor<ApproveSbj> {
}
