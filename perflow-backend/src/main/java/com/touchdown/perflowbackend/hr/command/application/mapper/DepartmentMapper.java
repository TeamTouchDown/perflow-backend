package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.application.dto.department.DepartmentCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Pic;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentListResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.DepartmentQueryResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DepartmentMapper {

    public static Department toEntity(DepartmentCreateDTO createDTO, Department managedDepartment) {

        return Department.builder()
                .createDTO(createDTO)
                .manageDept(managedDepartment)
                .build();
    }

    public static DepartmentQueryResponseDTO toResponse(Department department) {
        return DepartmentQueryResponseDTO.builder()
                .departmentId(department.getDepartmentId())
                .name(department.getName())
                .subDepartments(toResponseList(department.getSubDepartments()))
                .build();
    }

    private static List<DepartmentQueryResponseDTO> toResponseList(List<Department> departments) {
        return departments.stream()
                .map(DepartmentMapper::toResponse)
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
                    .contact(department.getContact())
                    .build();
            if (department.getManageDept() != null) {
                responseDTO.setManagedDeptId(department.getManageDept().getDepartmentId());
            }

            departmentListDTO.add(responseDTO);
        }

        return departmentListDTO;
    }

    public static Pic toPic(Department department, Employee employee) {

        return Pic.builder()
                .department(department)
                .employee(employee)
                .build();
    }
}
