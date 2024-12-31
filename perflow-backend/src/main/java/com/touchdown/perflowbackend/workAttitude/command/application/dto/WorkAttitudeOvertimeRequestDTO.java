package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeOvertimeRequestDTO {

    private String empId; // 사원 ID

    private String approverId; // 결재자 ID

    private OvertimeType overtimeType; // 초과 근무 유형 (야간, 휴일, 연장 등)

    private LocalDateTime enrollOvertime; // 신청 일자

    private LocalDateTime overtimeStart; // 초과 근무 시작 시간

    private LocalDateTime overtimeEnd; // 초과 근무 종료 시간

    private Boolean isOvertimeRetroactive; // 소급 여부 (기본값: false)

    private String overtimeRetroactiveReason; // 소급 사유 (있을 경우)
}
