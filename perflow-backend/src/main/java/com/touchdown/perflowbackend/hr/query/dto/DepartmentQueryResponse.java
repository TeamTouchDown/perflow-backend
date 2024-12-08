package com.touchdown.perflowbackend.hr.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DepartmentQueryResponse {

    private final Long departmentId;

    private final String name;

    private final List<DepartmentQueryResponse> subDepartments;

}
