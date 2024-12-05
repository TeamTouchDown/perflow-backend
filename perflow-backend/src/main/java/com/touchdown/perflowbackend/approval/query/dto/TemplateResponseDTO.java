package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class TemplateResponseDTO {

    private final Long templateId;

    private final String templateName;

    private final String description;

    private final LocalDateTime createDatetime;

    private final LocalDateTime updateDatetime;

    private final String empName;
}
