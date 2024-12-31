package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AttendanceStatus;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private AttendanceStatus attendanceStatus;


}


