package com.touchdown.perflowbackend.employee.command.Mapper;


import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeDetailResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeMapper {

    public static EmployeeQueryResponseDTO toResponse(Employee employee) {

        List<Long> authorities = getAuthorityIds(employee);

        return EmployeeQueryResponseDTO.builder()
                .empId(employee.getEmpId())
                .name(employee.getName())
                .position(employee.getPosition().getName())
                .job(employee.getJob().getName())
                .department(employee.getDept().getName())
                .authorities(authorities)
                .build();
    }

    public static EmployeeDetailResponseDTO toDetailResponse(Employee employee) {

        return EmployeeDetailResponseDTO.builder()
                .emp(employee)
                .build();
    }

    public static List<EmployeeQueryResponseDTO> toResponseList(List<Employee> employees) {

        return employees.stream()
                .map(EmployeeMapper::toResponse)
                .collect(Collectors.toList());
    }

    private static List<Long> getAuthorityIds(Employee employee) {

        return employee.getAuthorities().stream()
                .map(Authority::getAuthorityId)
                .collect(Collectors.toList());
    }
}
