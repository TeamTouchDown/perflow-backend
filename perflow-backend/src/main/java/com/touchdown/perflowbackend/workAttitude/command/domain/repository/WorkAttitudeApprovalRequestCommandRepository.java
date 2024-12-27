/*
package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkAttitudeApprovalRequestCommandRepository {

    Optional<ApprovalRequest> findById(Long requestId);

    ApprovalRequest save(ApprovalRequest approvalRequest);

    //날짜 중복 검증
    boolean existsByEmpId_EmpIdAndRequestTypeAndStatusAndDeleteStatusAndCreateDatetimeBetween(
            String empId, RequestType requestType, Status status, DeleteStatus deleteStatus,
            LocalDateTime startDate, LocalDateTime endDate
    );
    //연차 개수 조회
    long countByEmpId_EmpIdAndRequestTypeAndStatus(
            String empId, RequestType requestType, Status status
    );

}
*/
