package com.touchdown.perflowbackend.employee.command.application.dto;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeStatusUpdateDTO {

    private String empId;

    private EmployeeStatus status;
}
