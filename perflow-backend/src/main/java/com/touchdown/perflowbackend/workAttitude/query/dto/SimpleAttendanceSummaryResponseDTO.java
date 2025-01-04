package com.touchdown.perflowbackend.workAttitude.query.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimpleAttendanceSummaryResponseDTO {
    private String empId;
    private String empName;
    private int year;
    private int month;
    private int tardinessCount; // 지각 횟수
    private int absenceCount;    // 결근 횟수
}
