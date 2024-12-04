package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.FieldType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.TemplateField;

public class TemplateFieldMapper {

    public static TemplateField templateFieldToEntity(
            Template template,
            FieldType fieldType,
            String details,
            Boolean isRepeated,
            Long fieldOrder
    ) {

        return TemplateField.builder()
                .templateId(template)
                .fieldTypeId(fieldType)
                .details(details)
                .isRepeated(isRepeated)
                .fieldOrder(fieldOrder)
                .status(Status.ACTIVATED)
                .build();
    }

}
