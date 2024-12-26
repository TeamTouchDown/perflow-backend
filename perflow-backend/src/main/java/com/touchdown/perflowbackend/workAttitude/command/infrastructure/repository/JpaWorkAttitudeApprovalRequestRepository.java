package com.touchdown.perflowbackend.workAttitude.command.infrastructure.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.ApprovalRequest;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeApprovalRequestCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkAttitudeApprovalRequestRepository extends WorkAttitudeApprovalRequestCommandRepository, JpaRepository<ApprovalRequest,Long> {
}
