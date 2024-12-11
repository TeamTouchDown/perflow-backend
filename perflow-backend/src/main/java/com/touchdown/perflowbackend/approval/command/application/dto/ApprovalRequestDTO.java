package com.touchdown.perflowbackend.approval.command.application.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.SbjType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApprovalRequestDTO {

    private Long docId;

    private SbjType sbjType;    // EMPLOYEE, DEPARTMENT

    private String approveSbjId;  // 결재 주체 id

    private Status status;  // 승인, 반려...

    private String comment; // 승인/반려/보류 의견
}
