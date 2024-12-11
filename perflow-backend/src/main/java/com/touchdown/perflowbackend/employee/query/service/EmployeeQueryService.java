package com.touchdown.perflowbackend.employee.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.Mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeDetailResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeQueryService {

    private final EmployeeQueryRepository employeeQueryRepository;

    @Transactional(readOnly = true)
    public List<EmployeeQueryResponseDTO> readDeptEmployees(Long departmentId) {

        List<Employee> employees = findEmployeesByDepartmentId(departmentId);

        return EmployeeMapper.toResponseList(employees);
    }

    @Transactional(readOnly = true)
    public List<EmployeeQueryResponseDTO> readAllEmployees() {

        List<Employee> employees = findAllEmployee();

        return EmployeeMapper.toResponseList(employees);
    }

    @Transactional(readOnly = true)
    public EmployeeDetailResponseDTO readEmployeeDetail(String empId) {

        Employee employee = findById(empId);

        return EmployeeMapper.toDetailResponse(employee);
    }

    private Employee findById(String empId) {

        return employeeQueryRepository.findById(empId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );
    }

    private List<Employee> findEmployeesByDepartmentId(Long departmentId) {

        return employeeQueryRepository.findByDeptId(departmentId);
    }

    private List<Employee> findAllEmployee() {

        return employeeQueryRepository.findAll();
    }

}
