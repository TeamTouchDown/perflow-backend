package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TemplateFieldResponseDTO {

    private final Long templateFieldId;

    private final String details;

    private final Long fieldTypeId;

    private final String fieldType;

    private final Long fieldOrder;

    private final Boolean isRepeated;

    private final Status status;

}
