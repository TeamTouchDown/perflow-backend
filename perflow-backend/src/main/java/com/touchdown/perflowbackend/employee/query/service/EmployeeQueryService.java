package com.touchdown.perflowbackend.employee.query.service;

import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.Mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeDetailResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeQueryResponseDTO;
import com.touchdown.perflowbackend.employee.query.dto.EmployeeResponseList;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeQueryService {

    private final EmployeeQueryRepository employeeQueryRepository;

    @Transactional(readOnly = true)
    public List<EmployeeQueryResponseDTO> getDeptEmployees(Long departmentId) {

        List<Employee> employees = findEmployeesByDepartmentId(departmentId);

        return EmployeeMapper.toResponseList(employees);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseList getAllEmployees(Pageable pageable) {

        Page<Employee> pages = findAllEmployee(pageable);

        List<EmployeeQueryResponseDTO> employees = EmployeeMapper.toResponseList(pages.getContent());

        return EmployeeResponseList.builder()
                .employeeList(employees)
                .totalPages(pages.getTotalPages())
                .totalItems((int) pages.getTotalElements())
                .currentPage(pages.getNumber() + 1)
                .pageSize(pages.getSize())
                .build();
    }

    @Transactional(readOnly = true)
    public EmployeeResponseList getAllEmployeesByName(Pageable pageable, String name) {

        Page<Employee> pages = findEmployeeByName(pageable, name);

        List<EmployeeQueryResponseDTO> employees = EmployeeMapper.toResponseList(pages.getContent());

        return EmployeeResponseList.builder()
                .employeeList(employees)
                .totalPages(pages.getTotalPages())
                .totalItems((int) pages.getTotalElements())
                .currentPage(pages.getNumber() + 1)
                .pageSize(pages.getSize())
                .build();
    }

    @Transactional(readOnly = true)
    public EmployeeDetailResponseDTO getEmployeeDetail(String empId) {

        Employee employee = findById(empId);

        return EmployeeMapper.toDetailResponse(employee);
    }

    @Transactional(readOnly = true)
    public EmployeeResponseList getInvitedEmployees(Pageable pageable) {

        Page<Employee> pages = employeeQueryRepository.findEmployeeByStatus(pageable, EmployeeStatus.PENDING);

        List<EmployeeQueryResponseDTO> employees = EmployeeMapper.toResponseList(pages.getContent());

        return EmployeeResponseList.builder()
                .employeeList(employees)
                .totalPages(pages.getTotalPages())
                .totalItems((int) pages.getTotalElements())
                .currentPage(pages.getNumber() + 1)
                .pageSize(pages.getSize())
                .build();
    }

    private Employee findById(String empId) {

        Employee employee = employeeQueryRepository.findById(empId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );
        employee.getAuthorities().size(); // 권한을 초기화

        return employee;
    }

    private List<Employee> findEmployeesByDepartmentId(Long departmentId) {

        return employeeQueryRepository.findByDeptId(departmentId);
    }

    private Page<Employee> findAllEmployee(Pageable pageable) {

        return employeeQueryRepository.findAll(pageable);
    }

    private Page<Employee> findEmployeeByName(Pageable pageable, String name) {

        return employeeQueryRepository.findByNameContaining(name, pageable);
    }
}
