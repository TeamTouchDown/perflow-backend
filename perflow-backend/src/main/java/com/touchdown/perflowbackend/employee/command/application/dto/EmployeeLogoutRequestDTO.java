package com.touchdown.perflowbackend.employee.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeLogoutRequestDTO {

    private String empId;

    private String accessToken;
}
