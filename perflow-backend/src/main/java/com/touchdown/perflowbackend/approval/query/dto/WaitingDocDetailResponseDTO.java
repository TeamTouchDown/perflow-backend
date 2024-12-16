package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class WaitingDocDetailResponseDTO {

    private final Long docId;   // 문서 id

    private final String title; // 문서 제목

    private final Map<String, Object> fields;   // 필드 데이터 (키 - 값)

    private final List<WaitingDocApproveLineDTO> approveLines;    // 결재선 정보

    private final List<WaitingDocShareDTO> shares;    // 공유 설정 정보

}
