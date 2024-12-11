package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkAttitudeTravelCommandForTeamLeaderRequestDTO {

    @NotNull
    private String travelStatus;

    @Nullable
    private String rejectReason;
}
