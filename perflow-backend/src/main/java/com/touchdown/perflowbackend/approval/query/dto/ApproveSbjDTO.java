package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApproveSbjDTO {

    private final EmpDeptType empDeptType;

    private final String empId;

    private final Long departmentId;

    private final Status status;

}
