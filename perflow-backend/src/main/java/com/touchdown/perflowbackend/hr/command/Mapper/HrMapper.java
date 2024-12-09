package com.touchdown.perflowbackend.hr.command.Mapper;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentQueryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class HrMapper {

    public static DepartmentQueryResponseDTO toResponse(Department department) {
        return DepartmentQueryResponseDTO.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .subDepartments(toResponseList(department.getSubDepartments()))
                .build();
    }

    private static List<DepartmentQueryResponseDTO> toResponseList(List<Department> departments) {
        return departments.stream()
                .map(HrMapper::toResponse)
                .collect(Collectors.toList());
    }
}
