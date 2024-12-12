package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocCreateRequestDTO {

    private final String title;

//    private final String content;

    private final Long templateId;

    private final List<TemplateFieldDTO> fields;    // 필드 데이터

    private final List<ApproveLineDTO> approveLines;    // 결재선 정보

    private final List<ShareDTO> shares;    // 공유 설정 정보
}
