package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DocMapper {

    public static Doc toEntity(DocCreateRequestDTO request, Employee createUser, Template template) {

        // 문서 생성
        return Doc.builder()
                .title(request.getTitle())
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
                        .map(DocMapper::toApproveLineResponseDTO)
                        .toList())
                .build();
    }

    // 나의 결재선 상세 조회 시
    public static ApproveLineResponseDTO toApproveLineResponseDTO(ApproveLine line) {

        return ApproveLineResponseDTO.builder()
                .groupId(line.getGroupId())
                .approveLineId(line.getApproveLineId())
                .approveType(line.getApproveType())
                .approveLineOrder(line.getApproveLineOrder())
                .pllGroupId(line.getPllGroupId())
                .approveTemplateType(line.getApproveTemplateType())
                .approveSbjs(line.getApproveSbjs().stream()
                        .map(DocMapper::toApproveSbjDTO)
                        .toList())
                .build();
    }

//    public static WaitingDocListResponseDTO toWaitingDocListResponseDTO(Doc doc) {
//
//        return WaitingDocListResponseDTO.builder()
//                .docId(doc.getDocId())
//                .title(doc.getTitle())
//                .createUserName(doc.getCreateUser().getName())
//                .createDatetime(doc.getCreateDatetime())
//                .status(doc.getStatus())
//                .lines(doc.getApproveLines().stream()
//                        .map(DocMapper::toApproveLineDTO)
//                        .toList())
//                .build();
//    }
//
//    public static WaitingDocDetailResponseDTO toWaitingDocDetailResponseDTO(Doc doc) {
//
//        return WaitingDocDetailResponseDTO.builder()
//                .docId(doc.getDocId())
//                .title(doc.getTitle())
//                .templateId(doc.getTemplate().getTemplateId())
//                // 결재선
//                .approveLines(doc.getApproveLines().stream()
//                        .map(DocMapper::toApproveLineDetailDTO)
//                        .toList())
//                .shares(toShareDTOList(doc.getShares()))
//                .build();
//    }

    public static ApproveLineDetailDTO toApproveLineDetailDTO(ApproveLine line) {

        return ApproveLineDetailDTO.builder()
                .approveLineId(line.getApproveLineId())
                .approveType(line.getApproveType())
                .approveLineOrder(line.getApproveLineOrder())
                .approveSbjs(line.getApproveSbjs().stream()
                        .map(DocMapper::toApproveSbjDTO)
                        .toList())
                .build();
    }

    public static ApproveSbjDTO toApproveSbjDTO(ApproveSbj sbj) {

        return ApproveSbjDTO.builder()
                .empDeptType(sbj.getEmpDeptType())
                .empId(sbj.getSbjUser() != null ? sbj.getSbjUser().getEmpId() : null)   // sbjType에 따라 null 값 넘겨야 함
                .departmentId(sbj.getDept() != null ? sbj.getDept().getDepartmentId() : null)
                .build();
    }

    public static List<ShareDTO> toShareDTOList(List<DocShareObj> shares) {
        return shares.stream()
                .map(share -> new ShareDTO(
                        share.getShareEmpDeptType(),
                        share.getShareEmpDeptType() == EmpDeptType.DEPARTMENT && share.getShareObjDepartment() != null
                                ? List.of(share.getShareObjDepartment().getDepartmentId()) : new ArrayList<>(),
                        share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                ? List.of(share.getShareObjUser().getEmpId()) : new ArrayList<>()
                ))
                .toList();
    }

    public static ApproveSbj toApproveSbj(
            ApproveSbjDTO sbjDTO,
            Map<String, Employee> employeeMap,
            Map<Long, Department> departmentMap,
            ApproveType approveType) {

        Employee sbjUser = null;
        if (sbjDTO.getEmpId() != null) {
            sbjUser = employeeMap.get(sbjDTO.getEmpId());
            if (sbjUser == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_EMP);
            }
        }

        Department dept = null;
        if (sbjDTO.getDepartmentId() != null) {
            dept = departmentMap.get(sbjDTO.getDepartmentId());
            if (dept == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT);
            }
        }

        boolean isPll = approveType == ApproveType.PLL || approveType == ApproveType.PLLAGR;

        return ApproveSbj.builder()
                .empDeptType(sbjDTO.getEmpDeptType())
                .sbjUser(sbjUser)
                .dept(dept)
                .isPll(isPll)
                .build();

    }

    public static DocShareObj toDocShareObj(Doc doc, Employee createUser, EmpDeptType empDeptType, Employee employee, Department department) {

        return DocShareObj.builder()
                .doc(doc)
                .shareAddUser(createUser)
                .shareEmpDeptType(empDeptType)
                .shareObjUser(employee)
                .shareObjDepartment(department)
                .build();
    }
}
