package com.touchdown.perflowbackend.hr.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class DepartmentQueryResponseDTO {

    private final Long departmentId;

    private final String name;

    private final List<DepartmentQueryResponseDTO> subDepartments;

}
