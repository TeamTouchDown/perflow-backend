package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
@RequiredArgsConstructor
public class ProcessedDocDetailResponseDTO {

    private final Long docId;   // 문서 id

    private final String createUserName;

    private final String createUserDept;

    private final String createUserPosition;

    private final LocalDateTime createDatetime;

    private final String title; // 문서 제목

    private final Map<String, Object> fields;   // 필드 데이터 (키 - 값)

    private final List<ProcessedDocApproveLineDTO> approveLines;    // 결재선 정보

    private final List<ProcessedDocShareDTO> shares;    // 공유 설정 정보
}
