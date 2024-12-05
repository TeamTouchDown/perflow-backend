package com.touchdown.perflowbackend.employee.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeLoginResponseDTO {

    private String empId;

    private String accessToken;

    public EmployeeLoginResponseDTO(String empId, String accessToken) {
        this.empId = empId;
        this.accessToken = accessToken;
    }
}
