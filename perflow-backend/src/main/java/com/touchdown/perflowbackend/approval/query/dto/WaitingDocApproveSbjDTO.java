package com.touchdown.perflowbackend.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WaitingDocApproveSbjDTO {

    @JsonProperty("empDeptType")
    private final EmpDeptType empDeptType;

    @JsonProperty("empId")
    private final String empId;

    @JsonProperty("sbjName")
    private final String empName;   // 사원 이름
}
