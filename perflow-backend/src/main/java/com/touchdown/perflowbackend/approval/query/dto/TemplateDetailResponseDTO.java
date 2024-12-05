package com.touchdown.perflowbackend.approval.query.dto;


import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TemplateDetailResponseDTO {

    private final Long templateId;

    private final String templateName;

    private final String description;

    private final LocalDateTime createDatetime;

    private final LocalDateTime updateDatetime;

    private final String empName;

    private final Status status;

    private List<TemplateFieldResponseDTO> fields;
}
