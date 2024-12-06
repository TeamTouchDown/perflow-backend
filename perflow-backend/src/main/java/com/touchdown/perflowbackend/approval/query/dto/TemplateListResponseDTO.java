package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Builder
public class TemplateListResponseDTO {

    private final Long templateId;

    private final String templateName;

    private final String description;

    private final LocalDateTime createDatetime;

    private final LocalDateTime updateDatetime;

    private final String empName;

    private final Status status;
}