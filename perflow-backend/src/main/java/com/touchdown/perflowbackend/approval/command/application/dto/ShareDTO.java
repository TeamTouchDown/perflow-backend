package com.touchdown.perflowbackend.approval.command.application.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ShareDTO {

    private final EmpDeptType shareEmpDeptType;

    private final List<Long> departments;

    private final List<String> employees;
}
