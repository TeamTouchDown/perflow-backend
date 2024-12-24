package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.VacationStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.VacationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkAttitudeVacationResponseDTO {

    private Long vacationId;              // 휴가 ID

    private String empId;                 // 사원 ID

    private String empName;               // 사원 이름

    private Long approveSbjId;            // 결재 주제 ID

    private String approverName;          // 결재자 이름

    private LocalDateTime enrollVacation; // 신청일

    private LocalDateTime vacationStart;  // 시작일

    private LocalDateTime vacationEnd;    // 종료일

    private VacationType vacationType;    // 휴가 유형

    private VacationStatus vacationStatus; // 상태 (승인, 반려 등)

    private String vacationRejectReason;  // 반려 사유 (있으면)

    private Status status;
}
