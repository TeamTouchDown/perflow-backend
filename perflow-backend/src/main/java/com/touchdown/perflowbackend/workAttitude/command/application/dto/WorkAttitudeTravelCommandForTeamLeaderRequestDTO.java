package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

@Data
public class WorkAttitudeTravelCommandForTeamLeaderRequestDTO {


    private String travelStatus;

    @Nullable
    private String rejectReason;
}
