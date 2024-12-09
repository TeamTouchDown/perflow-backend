package com.touchdown.perflowbackend.employee.command.application.mapper;

import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import lombok.extern.slf4j.Slf4j;

import javax.swing.text.html.parser.Entity;

@Slf4j
public class EmployeeMapper {

    public static Employee toEntity(EmployeeRegisterDTO registerDTO, Position position, Job job, Department department) {

        log.info(registerDTO.toString());

        return Employee.builder()
                .registerDTO(registerDTO)
                .position(position)
                .job(job)
                .department(department)
                .build();
    }
}
