package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkAttitudeRetroactiveRequestDTO {

    @NotNull
    private Long overtimeId;

    @NotNull
    private String reason;
}