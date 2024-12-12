package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class WorkAttitudeAttendanceResponseDTO {
    @Null
    private Long attendanceId;

    @NotNull
    private String empId;

    @Nullable
    private LocalDateTime checkInDateTime; // 출근 시간

    @Nullable
    private LocalDateTime checkOutDateTime; // 퇴근 시간

    @NotNull
    private Status status;
}


