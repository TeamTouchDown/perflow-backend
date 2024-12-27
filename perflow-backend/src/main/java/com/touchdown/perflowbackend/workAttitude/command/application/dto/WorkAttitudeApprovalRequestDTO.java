/*
package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;
import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeApprovalRequestDTO {

    private String empId;              // 결재자 사원 ID

    private String approverId;         // 결재 항목의 승인자 ID

    private String rejectReason;       // 반려 사유

    private Status status;             // 승인/반려 상태

    private Long relatedId;

    private RequestType requestType;

    // ------------------------- 연차 관련 필드 -------------------------
    private LocalDateTime annualStart; // 연차 시작 날짜

    private LocalDateTime annualEnd;   // 연차 종료 날짜

    private AnnualType annualType;     // 연차 유형 (FULLDAY, MORNING_HALF, AFTERNOON_HALF)

    private String annualRejectReason; // 반려 사유

    private Boolean isAnnualRetroactive; // 소급 여부

    private String annualRetroactiveReason; // 소급 사유

    // ------------------------- 초과근무 관련 필드 -------------------------

    private OvertimeType overtimeType; // 초과근무 유형

    private LocalDateTime overtimeStart; // 초과근무 시작 날짜

    private LocalDateTime overtimeEnd;   // 초과근무 종료 날짜

    private Boolean isOvertimeRetroactive; // 초과근무 소급 여부

    private String overtimeRetroactiveReason; // 소급 사유

    private LocalDateTime enrollOvertime; // 초과근무 등록 일시

    private Status overtimeStatus;       // 초과근무 상태

    // ------------------------- 휴가 관련 필드 -------------------------

    private VacationType vacationType;   // 휴가 유형 (출산, 배우자 출산, 생리 휴가, 가족 돌봄 등)

    private LocalDateTime vacationStart; // 휴가 시작 날짜

    private LocalDateTime vacationEnd;   // 휴가 종료 날짜

    private String vacationRejectReason; // 반려 사유

    private VacationStatus vacationStatus; // 휴가 상태 (CONFIRMED, REJECTED, PENDING)

    // ------------------------- 출장 관련 필드 -------------------------

    private String travelReason;       // 출장 사유

    private LocalDateTime travelStart;  // 출장 시작일

    private LocalDateTime travelEnd;    // 출장 종료일

    private String travelRejectReason;  // 반려 사유

    private String travelDivision;      // 출장 구분 (해외, 국내)

}
*/
