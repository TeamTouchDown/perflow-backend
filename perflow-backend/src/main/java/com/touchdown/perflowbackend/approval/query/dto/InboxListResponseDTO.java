package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class InboxListResponseDTO {

    private final Long docId;            // 문서 ID

    private final String title;          // 문서 제목

    private final LocalDateTime createDatetime; // 작성일

    private final String senderName;     // 발신자

    private final String receiverName;  // 수신자 (내 이름 외 N명)

    private final LocalDateTime receivedDatetime; // 수신일

    private final Status status;         // 상태 (대기, 진행, 완료)

    private final LocalDateTime completedDatetime; // 완료일


}
