package com.touchdown.perflowbackend.security.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.CustomEmployDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomEmployeeDetailsService implements UserDetailsService {

    private final EmployeeCommandRepository employeeCommandRepository;

    @Override
    public UserDetails loadUserByUsername(String empId) throws UsernameNotFoundException {

        Employee employee = employeeCommandRepository.findById(empId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );

        return new CustomEmployDetail(employee);
    }
}
