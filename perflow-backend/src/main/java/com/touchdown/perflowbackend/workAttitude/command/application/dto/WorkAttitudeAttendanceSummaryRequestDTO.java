package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkAttitudeAttendanceSummaryRequestDTO {

    private String empId;

    private LocalDate startDate;

    private LocalDate endDate;

}
