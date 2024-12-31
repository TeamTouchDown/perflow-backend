package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class DocCreateRequestDTO {

    private final Long templateId;  // 서식 ID

    private final String title; // 문서 제목

    private final Map<String, String> fields;   // 키 - 쌍으로 구성

    private final List<ApproveLineRequestDTO> approveLines;    // 결재선 정보

    private final List<ShareDTO> shares;    // 공유 설정 정보
}
