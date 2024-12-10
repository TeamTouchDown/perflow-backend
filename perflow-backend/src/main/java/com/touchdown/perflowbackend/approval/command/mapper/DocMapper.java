package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.SbjType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineListResponseDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.util.List;

public class DocMapper {

    public static Doc toEntity(DocCreateRequestDTO request, Employee createUser, Template template) {

        // 문서 생성
        return Doc.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .template(template)
                .createUser(createUser)
                .build();
    }

    public static MyApproveLineListResponseDTO toMyApproveLineResponseDTO(ApproveLine approveLine) {

        List<String> employees = approveLine.getApproveSubjects().stream()
                .filter(sbj -> sbj.getSbjType() == SbjType.EMPLOYEE)
                .map(sbj -> sbj.getSbjUser().getName())
                .toList();

        List<String> departments = approveLine.getApproveSubjects().stream()
                .filter(sbj -> sbj.getSbjType() == SbjType.DEPARTMENT)
                .map(sbj -> sbj.getDept().getName())
                .toList();

        return MyApproveLineListResponseDTO.builder()
                .approveLineId(approveLine.getApproveLineId())
                .approveTemplateType(approveLine.getApproveTemplateType())
                .name(approveLine.getName())
                .description(approveLine.getDescription())
                .employees(employees)
                .departments(departments)
                .createDatetime(approveLine.getCreateDatetime())
                .build();
    }
}
