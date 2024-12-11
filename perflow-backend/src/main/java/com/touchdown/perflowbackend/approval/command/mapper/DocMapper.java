package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public static WaitingDocListResponseDTO toWaitingDocListResponseDTO(Doc doc) {

        return WaitingDocListResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .createUserName(doc.getCreateUser().getName())
                .createDatetime(doc.getCreateDatetime())
                .status(doc.getStatus())
                .lines(doc.getApproveLines().stream()
                        .map(DocMapper::toApproveLineDTO)
                        .toList())
                .build();
    }

    public static WaitingDocDetailResponseDTO toWaitingDocDetailResponseDTO(Doc doc) {

        return WaitingDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .content(doc.getContent())
                .templateId(doc.getTemplate().getTemplateId())
                // 결재선
                .approveLines(doc.getApproveLines().stream()
                        .map(DocMapper::toApproveLineDTO)
                        .toList())
                .shares(toShareDTOList(doc.getShares()))
                .build();
    }

    public static List<ShareDTO> toShareDTOList(List<DocShareObj> shares) {

        List<Long> departments = new ArrayList<>();
        List<String> employees = new ArrayList<>();

        for (DocShareObj share : shares) {
            if (share.getShareObjType() == ObjType.DEPARTMENT && share.getShareObjDepartment() != null) {
                departments.add(share.getShareObjDepartment().getDepartmentId());
            } else if (share.getShareObjType() == ObjType.EMPLOYEE && share.getShareObjUser() != null) {
                employees.add(share.getShareObjUser().getEmpId());
            }
        }

        return List.of(new ShareDTO(departments, employees));
    }
}
