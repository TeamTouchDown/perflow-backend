package com.touchdown.perflowbackend.approval.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApproveSbjDTO {

    @JsonProperty("empDeptType")
    private final EmpDeptType empDeptType;

    @JsonProperty("empId")
    private final String empId;
}
