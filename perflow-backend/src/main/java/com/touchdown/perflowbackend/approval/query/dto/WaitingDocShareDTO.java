package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class WaitingDocShareDTO {

    private final EmpDeptType shareEmpDeptType; // 공유 대상 종류(DEPARTMENT, EMPLOYEE)

    private final List<String> empIds;   // 공유 사원 목록

    private final List<String> empNames;   // 사원 이름 목록
}
