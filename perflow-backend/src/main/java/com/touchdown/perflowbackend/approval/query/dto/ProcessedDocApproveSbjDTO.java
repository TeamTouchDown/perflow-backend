package com.touchdown.perflowbackend.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProcessedDocApproveSbjDTO {

    private final EmpDeptType empDeptType;

    private final String empId;

    private final String empName;   // 사원 이름

    private final Long approveLineId;

    private final Long approveSbjId;

    private final Status status;

    private final String comment;

}
