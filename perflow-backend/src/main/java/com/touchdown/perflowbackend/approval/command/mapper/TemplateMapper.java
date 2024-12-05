package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.query.dto.TemplateResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.TemplateListResponseDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.util.List;

public class TemplateMapper {

    public static Template templateToEntity(Employee createEmp, TemplateCreateRequestDTO request) {

        return Template.builder()
                .createUser(createEmp)
                .name(request.getName())
                .description(request.getDescription())
                .status(Status.ACTIVATED)
                .build();
    }

    public static TemplateResponseDTO toTemplateResponseDTO(Template template) {
        return TemplateResponseDTO.builder()
                .templateId(template.getTemplateId())
                .templateName(template.getName())
                .description(template.getDescription())
                .createDatetime(template.getCreateDatetime())
                .updateDatetime(template.getUpdateDatetime())
                .empName(template.getCreateUser().getName())
                .build();
    }

    public static TemplateListResponseDTO toTemplateListResponseDTO(
            List<TemplateResponseDTO> templates, int currentPage, int totalPages
    ) {
        return TemplateListResponseDTO.builder()
                .templates(templates) // DTO 리스트
                .currentPage(currentPage) // 현재 페이지 번호
                .totalPages(totalPages) // 총 페이지 수
                .build();
    }
}
