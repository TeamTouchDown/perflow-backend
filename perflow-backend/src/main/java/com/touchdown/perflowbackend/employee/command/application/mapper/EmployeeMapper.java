package com.touchdown.perflowbackend.employee.command.application.mapper;

import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmployeeMapper {

    public static Employee toEntity(EmployeeCreateDTO registerDTO, Position position, Job job, Department department, String rrn) {

        return Employee.builder()
                .registerDTO(registerDTO)
                .position(position)
                .job(job)
                .department(department)
                .rrn(rrn)
                .build();
    }
}
