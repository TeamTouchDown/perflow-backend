package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class WorkAttributeOvertimeForTeamLeaderResponseDTO {

    @NotNull
    private Long overtimeId;

    @NotNull
    private String empId;

    @NotNull
    private String employeeName;

    @NotNull
    private OvertimeType overTimeType;

    @NotNull
    private LocalDateTime overtimeStart;

    @NotNull
    private LocalDateTime overtimeEnd;

    @NotNull
    private Boolean isOvertimeRetroactive;

    @Nullable
    private String overtimeRetroactiveReason;

    @NotNull
    private Status overtimeStatus;

    @Nullable
    private String rejectReason;

    @NotNull
    private Long approveSbjId;

    @NotNull
    private LocalDateTime createDatetime;

    @Nullable
    private LocalDateTime updateDatetime;

    @Nullable
    private LocalDateTime enrollOvertime;


}
