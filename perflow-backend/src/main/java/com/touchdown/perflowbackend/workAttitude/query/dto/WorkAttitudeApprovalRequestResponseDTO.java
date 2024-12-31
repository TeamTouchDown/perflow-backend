package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.DeleteStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.RequestType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WorkAttitudeApprovalRequestResponseDTO {

    private Long requestId;               // 요청 ID

    private String empId;                 // 신청자 ID

    private String approverId;            // 결재자 ID

    private Long relatedId;               // 관련 요청 ID

    private RequestType requestType;      // 요청 타입

    private Status status;                // 상태

    private String rejectReason;          // 반려 사유

    private DeleteStatus deleteStatus;    // 삭제 상태

    private LocalDateTime createDatetime; // 생성 일시

    private LocalDateTime updateDatetime; // 업데이트 일시

}
