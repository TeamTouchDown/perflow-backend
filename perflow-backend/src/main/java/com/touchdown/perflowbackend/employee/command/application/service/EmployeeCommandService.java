package com.touchdown.perflowbackend.employee.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final PositionCommandRepository positionCommandRepository;
    private final JobCommandRepository jobCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;

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



    }
}
