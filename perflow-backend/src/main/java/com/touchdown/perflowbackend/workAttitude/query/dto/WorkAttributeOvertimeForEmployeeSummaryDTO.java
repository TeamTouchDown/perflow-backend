package com.touchdown.perflowbackend.workAttitude.query.dto;


import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import jakarta.annotation.Nullable;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class WorkAttributeOvertimeForEmployeeSummaryDTO {
    private String employeeName; // 사원 이름
    private long nightHours; // 야간 근무 시간
    private long holidayHours; // 휴일 근무 시간
    private long extendedHours; // 연장 근무 시간
    private long totalHours; // 총 근무 시간
}
