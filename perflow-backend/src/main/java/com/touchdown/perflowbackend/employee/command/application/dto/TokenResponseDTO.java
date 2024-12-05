package com.touchdown.perflowbackend.employee.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenResponseDTO {

    private String empId;

    private String accessToken;

    private String refreshToken;

    public TokenResponseDTO(String empId, String accessToken, String refreshToken) {
        this.empId = empId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
