package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

public class TemplateMapper {

    public static Template templateToEntity(Employee createEmp, TemplateCreateRequestDTO request) {

        return Template.builder()
                .createUserId(createEmp)
                .name(request.getName())
                .description(request.getDescription())
                .status(Status.ACTIVATED)
                .build();
    }

}
