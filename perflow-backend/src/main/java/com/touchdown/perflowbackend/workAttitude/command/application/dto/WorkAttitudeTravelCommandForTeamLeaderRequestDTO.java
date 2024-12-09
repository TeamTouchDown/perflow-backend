package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WorkAttitudeTravelCommandForTeamLeaderRequestDTO {

    @NotNull // 해야되나 싶은데 일단 명시하는게 낫다고 하네요
    private String travelStatus;

    @Nullable
    private String rejectReason;
}
