package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkAttitudeOvertimeResponseDTO {

    private Long overtimeId; // 초과근무 ID

    private String empId; // 사원 ID

    private String empName; // 사원 이름

    private String approverId; // 결재자 ID

    private String approverName; // 결재자 이름

    private OvertimeType overtimeType; // 초과 근무 유형 (야간, 휴일, 연장 등)

    private LocalDateTime enrollOvertime; // 신청 일자

    private LocalDateTime overtimeStart; // 초과 근무 시작 시간

    private LocalDateTime overtimeEnd; // 초과 근무 종료 시간

    private Status overtimeStatus; // 초과 근무 상태 (승인, 반려 등)

    private String overtimeRejectReason; // 반려 사유 (있을 경우)

    private Boolean isOvertimeRetroactive; // 소급 여부

    private String overtimeRetroactiveReason; // 소급 사유

    private Status overtimeRetroactiveStatus; // 소급 상태 (대기, 승인, 반려)

    private Status status; // 상태 (활성, 삭제 등)
}
