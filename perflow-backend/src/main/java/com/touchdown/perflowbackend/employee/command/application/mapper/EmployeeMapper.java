package com.touchdown.perflowbackend.employee.command.application.mapper;

import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;

import javax.swing.text.html.parser.Entity;

public class EmployeeMapper {

    public static Employee toEntity(EmployeeRegisterDTO registerDTO, Position position, Job job, Department department) {

        return Employee.builder()
                .registerDTO(registerDTO)
                .position(position)
                .job(job)
                .department(department)
                .build();
    }
}