package com.touchdown.perflowbackend.approve.command.domain.infrastructure.repository;

import com.touchdown.perflowbackend.approve.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approve.command.domain.repository.ApproveCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaApproveCommandRepository extends ApproveCommandRepository, JpaRepository<ApproveSbj, Long> {

}
