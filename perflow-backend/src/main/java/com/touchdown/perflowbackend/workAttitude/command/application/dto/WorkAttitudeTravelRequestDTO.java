package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import jakarta.annotation.Nullable;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeTravelRequestDTO {

    private Long travelId;             // 출장 고유 번호

    private String empId;              // 사번

    private Long approveSbjId;         // 결재주체 고유 번호

    private LocalDateTime enrollTravel; // 신청 일자

    private String travelReason;       // 출장 사유

    private LocalDateTime travelStart; // 출장 시작일

    private LocalDateTime travelEnd;   // 출장 종료일

    private String travelStatus;       // 출장 결재 상태 (대기, 승인, 반려)

    @Nullable
    private String travelRejectReason; // 반려 사유

    private String travelDivision;     // 출장 구분 (해외, 국내)

    private LocalDateTime createDatetime; // 생성 일시

    @Nullable
    private LocalDateTime updateDatetime; // 수정 일시

    private String status;             // 상태

}
