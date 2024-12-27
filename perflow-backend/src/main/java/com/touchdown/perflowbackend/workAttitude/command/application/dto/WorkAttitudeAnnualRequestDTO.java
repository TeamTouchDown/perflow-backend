package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualRetroactiveStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeAnnualRequestDTO {

    private String empId; // 사원 ID

    private String approver; // 결재 주제 ID

    private LocalDateTime enrollAnnual; // 연차 등록 날짜

    private LocalDateTime annualStart; // 연차 시작 날짜

    private LocalDateTime annualEnd; // 연차 종료 날짜

    private AnnualType annualType; // 연차 유형 (FULLDAY, MORNING_HALF, AFTERNOON_HALF)

    private String annualRejectReason; // 반려 사유

    private Boolean isAnnualRetroactive; // 소급 여부

    private String annualRetroactiveReason; // 소급 사유

    private Status status; // activated, deleted 만 쓰는걸로

    private AnnualRetroactiveStatus annualRetroactiveStatus; // 소급 상태 (대기 승인 반려) pending confirmed rejected

}
