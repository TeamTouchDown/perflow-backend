package com.touchdown.perflowbackend.authority.application.service;

import com.touchdown.perflowbackend.authority.application.dto.AuthorityEmployeeRequestDTO;
import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.authority.domain.aggregate.AuthorityEmployee;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityEmployeeRepository;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class AuthorityEmployeeCommandService {

    private final AuthorityEmployeeRepository authorityEmployeeRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final AuthorityRepository authorityRepository;

    @Transactional
    public void createAuthorityEmployee(AuthorityEmployeeRequestDTO authorityEmployeeRequestDTO) {

        Employee employee = getEmployee(authorityEmployeeRequestDTO.getEmpId());
        Authority authority = getAuthority(authorityEmployeeRequestDTO.getAuthorityId());

        AuthorityEmployee authorityEmployee = new AuthorityEmployee(authority, employee);

        authorityEmployeeRepository.save(authorityEmployee);
    }

    @Transactional
    public void deleteAuthorityEmployee(AuthorityEmployeeRequestDTO authorityEmployeeRequestDTO) {

        Employee employee = getEmployee(authorityEmployeeRequestDTO.getEmpId());
        Authority authority = getAuthority(authorityEmployeeRequestDTO.getAuthorityId());
        AuthorityEmployee authorityEmployee = authorityEmployeeRepository.findByEmpAndAuthority(employee, authority);

        authorityEmployeeRepository.delete(authorityEmployee);
    }

    private Employee getEmployee(String empId) {

        return employeeCommandRepository.findById(empId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );
    }

    private Authority getAuthority(Long authorityId) {

        return authorityRepository.findById(authorityId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_AUTH)
        );
    }
}
