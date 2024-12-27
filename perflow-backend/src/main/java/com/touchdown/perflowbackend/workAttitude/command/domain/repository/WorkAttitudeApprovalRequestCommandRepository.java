package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkAttitudeApprovalRequestCommandRepository {

    Optional<ApprovalRequest> findById(Long requestId);

    ApprovalRequest save(ApprovalRequest approvalRequest);

    boolean existsByEmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween(String empId, RequestType requestType, Status status, DeleteStatus deleteStatus, LocalDateTime startDate, LocalDateTime endDate);
}
