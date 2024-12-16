package com.touchdown.perflowbackend.workAttitude.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ThreeMonthOvertimeDTO {

    private final String empId;

    private final Long nightHours; // 야간 근무 시간

    private final Long holidayHours; // 휴일 근무 시간

    private final Long extendedHours; // 연장 근무 시간

}
