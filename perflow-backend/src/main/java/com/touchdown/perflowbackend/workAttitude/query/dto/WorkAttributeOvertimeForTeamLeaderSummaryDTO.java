package com.touchdown.perflowbackend.workAttitude.query.dto;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class WorkAttributeOvertimeForTeamLeaderSummaryDTO {


    private String employeeName; // 사원 이름

    private long nightHours; // 야간 근무 시간

    private long holidayHours; // 휴일 근무 시간

    private long extendedHours; // 연장 근무 시간

    private long totalHours; // 총 근무 시간
}