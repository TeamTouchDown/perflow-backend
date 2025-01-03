package com.touchdown.perflowbackend.notification.command.application.dto;

import lombok.Data;

@Data
public class FcmTokenRequestDTO {

    private String empId;

    private String token;

    private String deviceType;
}
