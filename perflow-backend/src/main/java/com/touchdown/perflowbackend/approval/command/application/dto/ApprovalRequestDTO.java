package com.touchdown.perflowbackend.approval.command.application.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class ApprovalRequestDTO {

    private final Long docId;

    private final Long approveLineId;   // 결재선 id

    private final Long approveSbjId;    // 결재 주체 id

    private final EmpDeptType empDeptType;    // EMPLOYEE, DEPARTMENT

    private final String empId;  // 결재 주체 id

    private final Status status;  // 승인, 반려...

    private final String comment; // 승인/반려 의견
}
