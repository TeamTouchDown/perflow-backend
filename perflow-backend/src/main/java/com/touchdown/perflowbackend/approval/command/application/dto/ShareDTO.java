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

    private final EmpDeptType shareEmpDeptType; // 공유 대상 종류(DEPARTMENT, EMPLOYEE)

//    private final List<Long> departments;   // 공유 부서 목록

    private final List<String> employees;   // 공유 사원 목록
}
