package com.touchdown.perflowbackend.employee.command.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EmpRegisterDTO {

    private String empId;

    private Long positionId;


}
