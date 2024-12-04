package com.touchdown.perflowbackend.employee.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeLoginRequestDTO {

    private String empId;

    private String password;
}
