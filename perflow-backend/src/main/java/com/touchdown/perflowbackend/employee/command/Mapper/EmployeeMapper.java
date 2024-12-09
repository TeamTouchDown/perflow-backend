package com.touchdown.perflowbackend.employee.command.Mapper;


import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeQueryResponseDTO toResponse(Employee employee) {

        return EmployeeQueryResponseDTO.builder()
                .empId(employee.getEmpId())
                .name(employee.getName())
                .position(employee.getPosition().getName())
                .build();
    }

    public static List<EmployeeQueryResponseDTO> toResponseList(List<Employee> employees) {

        return employees.stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }
}
