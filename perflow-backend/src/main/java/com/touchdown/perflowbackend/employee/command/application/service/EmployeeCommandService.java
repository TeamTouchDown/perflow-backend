package com.touchdown.perflowbackend.employee.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeLoginRequestDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeLoginResponseDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeePwdRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import com.touchdown.perflowbackend.security.util.CustomEmployDetail;
import com.touchdown.perflowbackend.security.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final PositionCommandRepository positionCommandRepository;
    private final JobCommandRepository jobCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerEmployee(EmployeeRegisterDTO employeeRegisterDTO) {

        Department department = departmentCommandRepository.findById(employeeRegisterDTO.getDepartmentId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT)
        );

        Position position = positionCommandRepository.findById(employeeRegisterDTO.getPositionId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POSITION)
        );
        Job job = jobCommandRepository.findById(employeeRegisterDTO.getJobId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_JOB)
        );

        Employee newEmployee = EmployeeMapper.toEntity(employeeRegisterDTO, position, job, department);

        employeeCommandRepository.save(newEmployee);

    }


    @Transactional
    public EmployeeLoginResponseDTO loginRequestEmployee(EmployeeLoginRequestDTO employeeLoginRequestDTO) {

        // 1. AuthenticationManager를 통해 인증 수행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        employeeLoginRequestDTO.getEmpId(),
                        employeeLoginRequestDTO.getPassword()
                )
        );

        CustomEmployDetail customEmployDetail = (CustomEmployDetail) authentication.getPrincipal();

        String empId = customEmployDetail.getUsername();
        EmployeeStatus status = customEmployDetail.getStatus();

        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", empId);
        claims.put("status", status.name());

        String accessToken = jwtTokenProvider.createAccessToken(customEmployDetail.getUsername(), claims);

        return new EmployeeLoginResponseDTO(empId, accessToken);
    }

    @Transactional
    public void registerEmployeePassword(EmployeePwdRegisterDTO employeePwdRegisterDTO) {

        Employee employee = employeeCommandRepository.findById(employeePwdRegisterDTO.getEmpId()).orElseThrow(

                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );

        log.info(employee.toString());
        String pwd = passwordEncoder.encode(employeePwdRegisterDTO.getPassword());

        employee.registerPassword(pwd);

        employeeCommandRepository.save(employee);
    }
}
