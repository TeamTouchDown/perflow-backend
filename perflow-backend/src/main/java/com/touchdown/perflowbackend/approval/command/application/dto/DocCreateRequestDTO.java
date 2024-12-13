package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DocCreateRequestDTO {

    private final Long templateId;  // 서식 ID

    private final String title; // 문서 제목

    private final List<TemplateFieldDTO> fields;    // 필드 데이터

    private final List<ApproveLineRequestDTO> approveLines;    // 결재선 정보

    private final List<ShareDTO> shares;    // 공유 설정 정보
}
