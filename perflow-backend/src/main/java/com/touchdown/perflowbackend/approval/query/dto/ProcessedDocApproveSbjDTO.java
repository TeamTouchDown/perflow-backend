package com.touchdown.perflowbackend.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProcessedDocApproveSbjDTO {

    @JsonProperty("empDeptType")
    private final EmpDeptType empDeptType;

    @JsonProperty("empId")
    private final String empId;

    @JsonProperty("sbjName")
    private final String empName;   // 사원 이름

    @JsonProperty("approve_line_id")
    private final Long approveLineId;

    @JsonProperty("approve_sbj_id")
    private final Long approveSbjId;

    @JsonProperty("status")
    private final Status status;

    @JsonProperty("comment")
    private final String comment;

}
