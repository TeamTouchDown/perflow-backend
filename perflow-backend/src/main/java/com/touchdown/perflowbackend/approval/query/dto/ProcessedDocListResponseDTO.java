package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ProcessedDocListResponseDTO {

    private final Long docId;

    private final String title;

    private final String createUserName;

    private final String empId;

    private final Long approveLineId;

    private final Long approveSbjId;

    private final LocalDateTime createDatetime;

    private final LocalDateTime processDatetime;    // 승인/반려한 시간
}
