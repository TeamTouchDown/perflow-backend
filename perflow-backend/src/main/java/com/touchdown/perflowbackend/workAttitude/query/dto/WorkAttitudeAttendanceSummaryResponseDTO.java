package com.touchdown.perflowbackend.workAttitude.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkAttitudeAttendanceSummaryResponseDTO {

    private String period;

    private int totalHours;

    private int totalMinutes;

    private  String empId;

    private String empName;

    private int year;


}
