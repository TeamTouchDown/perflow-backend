package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.VacationStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.VacationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeVacationRequestDTO {

    private String empId; // 사원 ID

    private String approver; // 결재자 ID

    private LocalDateTime enrollVacation; // 휴가 등록 날짜

    private VacationType vacationType; // 휴가 유형 (출산, 배우자 출산, 생리 휴가, 가족 돌봄)

    private LocalDateTime vacationStart; // 휴가 시작 날짜

    private LocalDateTime vacationEnd; // 휴가 종료 날짜

    private VacationStatus vacationStatus; // 휴가 상태 (CONFIRMED, REJECTED, PENDING)

    private String vacationRejectReason; // 반려 사유

    private Status status; // 상태 (ACTIVATED, DELETED, UPDATED)

}
