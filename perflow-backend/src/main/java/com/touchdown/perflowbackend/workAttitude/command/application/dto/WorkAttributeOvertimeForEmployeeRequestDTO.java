package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkAttributeOvertimeForEmployeeRequestDTO {

    @NotNull
    private String empId;

    @NotNull
    private Long approveSbjId;

    @NotNull
    private String overtimeType;

    @NotNull
    private LocalDateTime overtimeStart;

    @NotNull
    private LocalDateTime overtimeEnd;

    private Boolean isOvertimeRetroactive;

    private String overtimeRetroactiveReason;

    @NotNull
    private LocalDateTime enrollOvertime;


}
