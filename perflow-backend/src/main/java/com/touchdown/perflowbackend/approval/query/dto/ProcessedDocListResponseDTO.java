package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProcessedDocListResponseDTO {

    private final Long docId;

    private final Long templateId;

    private final String title;

    private final String createUserName;

    private final String empId;

    private final Long approveLineId;

    private final Long approveSbjId;

    private final LocalDateTime createDatetime;

    private final Status approveSbjStatus;  // 승인/반려 여부

    private final LocalDateTime processDatetime;    // 승인/반려한 시간

    private final String comment;

    private Status status;  // 문서 상태
}
