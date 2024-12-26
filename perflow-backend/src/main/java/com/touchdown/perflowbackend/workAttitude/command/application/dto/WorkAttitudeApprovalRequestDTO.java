package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.DeleteStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.RequestType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeApprovalRequestDTO {

    private String empId;                 // 신청자 ID

    private String approverId;            // 결재자 ID

    private Long relatedId;               // 관련 요청 ID (연차, 휴가, 출장 등 PK)

    private RequestType requestType;           // 요청 타입 ('ANNUAL', 'VACATION', 'OVERTIME', 'TRAVEL')

    private Status status;                // 상태 ('PENDING', 'APPROVED', 'REJECTED')

    private String rejectReason;          // 반려 사유 (NULL 가능)

    private DeleteStatus deleteStatus;          // 삭제 상태 ('ACTIVATED', 'DELETED')

    private LocalDateTime createDatetime; // 생성 일시

    private LocalDateTime updateDatetime; // 업데이트 일시
}
