package com.touchdown.perflowbackend.hr.command.Mapper;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentListResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentQueryResponseDTO;

import java.util.ArrayList;
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

    // 모든 부서 >> 반환 DTO로 변경
    public static List<DepartmentListResponseDTO> toListDTO(List<Department> departments) {

        List<DepartmentListResponseDTO> departmentListDTO = new ArrayList<>();

        for (Department department : departments) {

            DepartmentListResponseDTO responseDTO = DepartmentListResponseDTO
                    .builder()
                    .name(department.getName())
                    .deptId(department.getDepartmentId())
                    .responsibility(department.getResponsibility())
                    .pic(department.getPic())
                    .contact(department.getContact())
                    .build();
            if (department.getManageDept() != null) {
                responseDTO.setManagedDeptId(department.getManageDept().getDepartmentId());
            }

            departmentListDTO.add(responseDTO);
        }

        return departmentListDTO;
    }
}
