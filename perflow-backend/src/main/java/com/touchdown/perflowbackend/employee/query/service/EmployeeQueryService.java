package com.touchdown.perflowbackend.employee.query.service;

import com.touchdown.perflowbackend.employee.command.Mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeQueryService {

    private final EmployeeQueryRepository employeeQueryRepository;

    public List<EmployeeQueryResponseDTO> readAllEmployees(Long departmentId) {

        List<Employee> employees = findEmployeesByDepartmentId(departmentId);

        return EmployeeMapper.toResponseList(employees);
    }

    private List<Employee> findEmployeesByDepartmentId(Long departmentId) {

        return employeeQueryRepository.findByDeptId(departmentId);
    }
}
