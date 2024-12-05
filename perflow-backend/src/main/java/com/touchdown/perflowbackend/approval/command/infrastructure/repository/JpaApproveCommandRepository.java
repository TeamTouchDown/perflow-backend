package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaApproveCommandRepository extends ApproveCommandRepository, JpaRepository<ApproveSbj, Long> {

}
