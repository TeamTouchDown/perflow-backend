package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.SbjType;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineGroupResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineResponseDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.time.LocalDateTime;
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

    public static MyApproveLineResponseDTO toMyApproveLineResponseDTO(ApproveLine approveLine) {

        return MyApproveLineResponseDTO.builder()
                .approveLineId(approveLine.getApproveLineId())
                .approveType(approveLine.getApproveType())
                .approveTemplateType(approveLine.getApproveTemplateType())
                .approveLineOrder(approveLine.getApproveLineOrder())
                .build();
    }

    public static MyApproveLineGroupResponseDTO toMyApproveLineGroupResponseDTO(
            Long groupId,
            String name,
            String description,
            LocalDateTime createDatetime,
            List<ApproveLine> lines
    ) {
        List<MyApproveLineResponseDTO> lineDTOs = lines.stream()
                .map(DocMapper::toMyApproveLineResponseDTO)
                .toList();

        return MyApproveLineGroupResponseDTO.builder()
                .groupId(groupId)
                .name(name)
                .description(description)
                .createDatetime(createDatetime)
                .lines(lineDTOs)
                .build();
    }

    // ApproveLine 엔터티 리스트 -> MyApproveLineDetailResponseDTO
    public static MyApproveLineDetailResponseDTO toMyApproveLineDetailResponseDTO(List<ApproveLine> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("결재선 empty");
        }

        return MyApproveLineDetailResponseDTO.builder()
                .name(lines.get(0).getName())
                .description(lines.get(0).getDescription())
                .approveLines(lines.stream()
                        .map(DocMapper::toApproveLineDTO)
                        .toList())
                .build();
    }

    public static ApproveLineDTO toApproveLineDTO(ApproveLine line) {

        return ApproveLineDTO.builder()
                .approveLineId(line.getApproveLineId())
                .approveType(line.getApproveType())
                .approveLineOrder(line.getApproveLineOrder())
                .pllGroupId(line.getPllGroupId())
                .departments(line.getApproveSubjects().stream()
                        .filter(subject -> subject.getSbjType() == SbjType.DEPARTMENT)
                        .map(subject -> subject.getDept().getDepartmentId())
                        .toList())
                .employees(line.getApproveSubjects().stream()
                        .filter(subject -> subject.getSbjType() == SbjType.EMPLOYEE)
                        .map(subject -> subject.getSbjUser().getEmpId())
                        .toList())
                .approveTemplateTypes(line.getApproveTemplateType())
                .build();
    }

}
