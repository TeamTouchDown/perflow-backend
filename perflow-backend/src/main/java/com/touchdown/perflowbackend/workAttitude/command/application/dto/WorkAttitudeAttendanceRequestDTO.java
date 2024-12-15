package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AttendanceStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeAttendanceRequestDTO {

    @Null
    private Long attendanceId;

    @NotNull
    private String empId;

    @Nullable
    private LocalDateTime checkInDateTime;

    @Nullable
    private LocalDateTime checkOutDateTime;

    @NotNull
    private AttendanceStatus AttendanceStatus;

}