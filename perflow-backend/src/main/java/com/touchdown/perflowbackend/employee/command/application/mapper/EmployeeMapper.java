package com.touchdown.perflowbackend.employee.command.application.mapper;

import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;

public class EmployeeMapper {

    public Employee toEntity(EmployeeRegisterDTO registerDTO, Position position, Job job, Department department) {
        
    }
}
