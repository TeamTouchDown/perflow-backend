package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InboxDocListResponseDTO {

    private final Long docId;

    private final String title;

    private final String createUserName;

    private final LocalDateTime createDatetime;

    private final LocalDateTime processDatetime;

    private final String status;    // 문서 상태(진행, 반려, 승인)

    private final Long approveLineId;

    private final Long approveSbjId;
}
