package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OutboxDocApproveSbjDTO {

    private final EmpDeptType empDeptType; // 대상 타입 (EMPLOYEE/DEPARTMENT)

    private final String empId; // 결재 대상 ID

    private final String empName; // 결재 대상 이름

    private final Long approveLineId; // 결재선 ID

    private final Long approveSbjId; // 결재 대상 ID

    private final String status; // 결재 상태

    private final String comment; // 의견
}
