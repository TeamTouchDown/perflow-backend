package com.touchdown.perflowbackend.Approval.command.mapper;

import com.touchdown.perflowbackend.Approval.command.application.dto.TemplateFieldCreateRequestDTO;
import com.touchdown.perflowbackend.Approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.Approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.Approval.command.domain.aggregate.TemplateField;

import java.util.List;
import java.util.stream.Collectors;

public class TemplateFieldMapper {

    public static TemplateField templateFieldToEntity(Template template, TemplateFieldCreateRequestDTO request) {

        return TemplateField.builder()
                .templateId(template)
                .name(request.getName())
                .type(request.getType())
                .isReq(request.getIsReq())
                .defaultValue(request.getDefaultValue())
                .status(Status.ACTIVATED)
                .build();
    }

    public static List<TemplateField> templateFieldsToEntities(Template template, List<TemplateFieldCreateRequestDTO> fieldRequests) {

        return fieldRequests.stream()
                .map(fieldRequest -> templateFieldToEntity(template, fieldRequest))
                .collect(Collectors.toList());
    }
}
