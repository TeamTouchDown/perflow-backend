package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OutboxDocListResponseDTO {

    private final Long docId; // 문서 ID

    private final String title; // 문서 제목

    private final LocalDateTime createDatetime; // 생성일

    private final Status status; // 문서 상태
}
