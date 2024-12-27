package com.touchdown.perflowbackend.approval.command.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class ApprovalRequestDTO {

    @JsonProperty("docId")
    private final Long docId;

    @JsonProperty("approveLineId")
    private final Long approveLineId;   // 결재선 id

    @JsonProperty("approveSbjId")
    private final Long approveSbjId;    // 결재 주체 id

    @JsonProperty("empDeptType")
    private final EmpDeptType empDeptType;    // EMPLOYEE, DEPARTMENT

    @JsonProperty("empId")
    private final String empId;  // 결재 주체 id

    @JsonProperty("status")
    private final Status status;  // 승인, 반려...

    @JsonProperty("comment")
    private final String comment; // 승인/반려 의견
}
