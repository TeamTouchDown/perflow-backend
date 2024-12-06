package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.TemplateUpdateRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.FieldType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.TemplateField;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class TemplateFieldMapper {

    public static TemplateField templateFieldToEntity(
            Template template,
            FieldType fieldType,
            String details,
            Boolean isRepeated,
            Long fieldOrder
    ) {

        return TemplateField.builder()
                .template(template)
                .fieldType(fieldType)
                .details(details)
                .isRepeated(isRepeated)
                .fieldOrder(fieldOrder)
                .status(Status.ACTIVATED)
                .build();
    }

}
