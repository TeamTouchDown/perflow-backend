package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.util.stream.Collectors;

public class TemplateMapper {

//    public static Template templateToEntity(Employee createEmp, TemplateCreateRequestDTO request) {
//
//        return Template.builder()
//                .createUser(createEmp)
//                .name(request.getName())
//                .description(request.getDescription())
//                .status(Status.ACTIVATED)
//                .build();
//    }
//
//    public static TemplateListResponseDTO toTemplateResponseDTO(Template template) {
//
//        return TemplateListResponseDTO.builder()
//                .templateId(template.getTemplateId())
//                .templateName(template.getName())
//                .description(template.getDescription())
//                .createDatetime(template.getCreateDatetime())
//                .updateDatetime(template.getUpdateDatetime())
//                .empName(template.getCreateUser().getName())
//                .status(template.getStatus())
//                .build();
//    }
//
//    public static TemplateDetailResponseDTO toTemplateDetailResponseDTO(Template template) {
//
//        return TemplateDetailResponseDTO.builder()
//                .templateId(template.getTemplateId())
//                .templateName(template.getName())
//                .description(template.getDescription())
//                .createDatetime(template.getCreateDatetime())
//                .updateDatetime(template.getUpdateDatetime())
//                .empName(template.getCreateUser().getName())
//                .status(template.getStatus())
//                .fields(template.getFields().stream()
//                        .map(TemplateMapper::toTemplateFieldResponseDTO)
//                        .collect(Collectors.toList()))
//                .build();
//    }
//
//    public static TemplateFieldResponseDTO toTemplateFieldResponseDTO(TemplateField field) {
//
//        return TemplateFieldResponseDTO.builder()
//                .templateFieldId(field.getTemplateFieldId())
//                .details(field.getDetails())
//                .fieldTypeId(field.getFieldType().getFieldTypeId())
//                .fieldType(field.getFieldType().getType())
//                .fieldOrder(field.getFieldOrder())
//                .isRepeated(field.getIsRepeated())
//                .status(field.getStatus())
//                .build();
//    }
}
