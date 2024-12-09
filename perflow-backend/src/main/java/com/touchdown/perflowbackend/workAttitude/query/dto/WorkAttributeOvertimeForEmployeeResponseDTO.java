package com.touchdown.perflowbackend.workAttitude.query.dto;


import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class WorkAttributeOvertimeForEmployeeResponseDTO {

    @NotNull
    private Long overtimeId;

    @NotNull
    private String empId;

    @NotNull
    private String employeeName;

    @NotNull
    private String overTimeType; // 이걸 스트링으로 받아야되나 status로 처리를 해야되나 고민이네요~ 야간 연장 휴일

    @NotNull
    private LocalDateTime overtimeStart;

    @NotNull
    private LocalDateTime overtimeEnd;

    @NotNull
    private Boolean isOvertimeRetroactive;

    @Nullable
    private String overtimeRetroactiveReason;

    @NotNull
    private String overtimeStatus;

    @Nullable
    private String rejectReason;

    @NotNull
    private String approveSbjName;

    @NotNull
    private LocalDateTime createDatetime;

    @Nullable
    private LocalDateTime updateDatetime;








}
