package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

//    public static MyApproveLineGroupResponseDTO toMyApproveLineGroupResponseDTO(
//            Long groupId,
//            String name,
//            String description,
//            LocalDateTime createDatetime,
//            List<ApproveLine> lines
//    ) {
//        List<MyApproveLineResponseDTO> lineDTOs = lines.stream()
//                .map(DocMapper::toMyApproveLineResponseDTO)
//                .toList();
//
//        return MyApproveLineGroupResponseDTO.builder()
//                .groupId(groupId)
//                .name(name)
//                .description(description)
//                .createDatetime(createDatetime)
//                .lines(lineDTOs)
//                .build();
//    }

    // 나의 결재선 상세 조회 시
    public static MyApproveLineDetailResponseDTO toMyApproveLineDetailResponseDTO(List<ApproveLine> lines, ApproveLine firstLine) {

        return MyApproveLineDetailResponseDTO.builder()
                .groupId(firstLine.getGroupId())
                .name(firstLine.getName())
                .description(firstLine.getDescription())
                .approveLines(lines.stream()
                        .map(DocMapper::toApproveLineResponseDTO)
                        .toList())
                .build();
    }

    public static MyApproveLineDTO toApproveLineResponseDTO(ApproveLine line) {

        return MyApproveLineDTO.builder()
                .approveLineId(line.getApproveLineId())
                .groupId(line.getGroupId())
                .approveType(line.getApproveType())
                .approveLineOrder(line.getApproveLineOrder())
                .pllGroupId(line.getPllGroupId())
                .approveTemplateType(line.getApproveTemplateType())
                .approveSbjs(line.getApproveSbjs().stream()
                        .map(DocMapper::toApproveSbjDTO)
                        .toList())
                .build();
    }

    public static MyApproveSbjDTO toApproveSbjDTO(ApproveSbj sbj) {

        return MyApproveSbjDTO.builder()
                .empDeptType(sbj.getEmpDeptType())
                .empId(sbj.getSbjUser().getEmpId())
                .empName(sbj.getSbjUser().getName())
                .approveLineId(sbj.getApproveLine().getApproveLineId())
                .approveSbjId(sbj.getApproveSbjId())
                .build();
    }

    // 대기 문서 목록 조회 시
    public static WaitingDocListResponseDTO toWaitingDocListResponseDTO(Doc doc) {

        return WaitingDocListResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .createUserName(doc.getCreateUser().getName())
                .createDatetime(doc.getCreateDatetime())
                .build();
    }

    // 처리 문서 목록 조회 시
    public static ProcessedDocListResponseDTO toProcessedDocListResponseDTO(Doc doc) {

        return ProcessedDocListResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .createUserName(doc.getCreateUser().getName())
                .createDatetime(doc.getCreateDatetime())
                .build();
    }

    // 대기 문서 상세 조회 시
    public static WaitingDocDetailResponseDTO toWaitingDocDetailResponseDTO(Doc doc) {
        return WaitingDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .fields(mapFields(doc.getDocFields())) // 필드 데이터 매핑
                .approveLines(mapWaitingApproveLines(doc.getApproveLines())) // 결재선 매핑
                .shares(mapWaitingShares(doc.getShares())) // 공유 설정 매핑
                .build();
    }

    public static ProcessedDocDetailResponseDTO toProcessedDocDetailResponseDTO(Doc doc) {

        return ProcessedDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .fields(mapFields(doc.getDocFields())) // 필드 데이터 매핑
                .approveLines(mapProcessedApproveLines(doc.getApproveLines())) // 결재선 매핑
                .shares(mapProcessedShares(doc.getShares())) // 공유 설정 매핑
                .build();
    }

    private static List<ProcessedDocShareDTO> mapProcessedShares(List<DocShareObj> shares) {

        return shares.stream()
                .filter(share -> share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null)
                .map(share -> new ProcessedDocShareDTO(
                        EmpDeptType.EMPLOYEE,
                        List.of(share.getShareObjUser().getEmpId()),
                        List.of(share.getShareObjUser().getName())
                ))
                .toList();
    }

    private static List<ProcessedDocApproveLineDTO> mapProcessedApproveLines(List<ApproveLine> approveLines) {

        return approveLines.stream()
                .map(line -> ProcessedDocApproveLineDTO.builder()
                        .approveLineId(line.getApproveLineId())
                        .groupId(line.getGroupId())
                        .approveType(line.getApproveType())
                        .approveLineOrder(line.getApproveLineOrder())
                        .pllGroupId(line.getPllGroupId())
                        .approveTemplateType(line.getApproveTemplateType())
                        .approveSbjs(mapProcessedApproveSubjects(line.getApproveSbjs(), line.getApproveLineId())) // 결재 주체 매핑
                        .build())
                .collect(Collectors.toList());
    }

    private static List<ProcessedDocApproveSbjDTO> mapProcessedApproveSubjects(List<ApproveSbj> approveSbjs, Long approveLineId) {

        return approveSbjs.stream()
                .map(sbj -> ProcessedDocApproveSbjDTO.builder()
                        .empDeptType(sbj.getEmpDeptType())
                        .empId(sbj.getSbjUser().getEmpId())
                        .empName(sbj.getSbjUser().getName())
                        .approveLineId(approveLineId)
                        .approveSbjId(sbj.getApproveSbjId())
                        .status(sbj.getStatus())
                        .comment(sbj.getComment())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<WaitingDocShareDTO> mapWaitingShares(List<DocShareObj> shares) {

        return shares.stream()
                .filter(share -> share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null)
                .map(share -> new WaitingDocShareDTO(
                        EmpDeptType.EMPLOYEE,
                        List.of(share.getShareObjUser().getEmpId()),
                        List.of(share.getShareObjUser().getName())
                ))
                .toList();
    }

    private static List<WaitingDocApproveLineDTO> mapWaitingApproveLines(List<ApproveLine> approveLines) {

        return approveLines.stream()
                .map(line -> WaitingDocApproveLineDTO.builder()
                        .approveLineId(line.getApproveLineId())
                        .groupId(line.getGroupId())
                        .approveType(line.getApproveType())
                        .approveLineOrder(line.getApproveLineOrder())
                        .pllGroupId(line.getPllGroupId())
                        .approveTemplateType(line.getApproveTemplateType())
                        .approveSbjs(mapWaitingApproveSubjects(line.getApproveSbjs(), line.getApproveLineId())) // 결재 주체 매핑
                        .build())
                .collect(Collectors.toList());
    }

    private static List<WaitingDocApproveSbjDTO> mapWaitingApproveSubjects(List<ApproveSbj> approveSbjs, Long approveLineId) {

        return approveSbjs.stream()
                .map(sbj -> WaitingDocApproveSbjDTO.builder()
                        .empDeptType(sbj.getEmpDeptType())
                        .empId(sbj.getSbjUser().getEmpId())
                        .empName(sbj.getSbjUser().getName())
                        .approveLineId(approveLineId)
                        .approveSbjId(sbj.getApproveSbjId())
                        .status(sbj.getStatus())
                        .comment(sbj.getComment())
                        .build())
                .collect(Collectors.toList());
    }

    private static Map<String, Object> mapFields(List<DocField> docFields) {

        return docFields.stream()
                .collect(Collectors.toMap(DocField::getFieldKey, DocField::getUserValue));
    }

    public static List<ShareDTO> toShareDTOList(List<DocShareObj> shares) {
        return shares.stream()
                .map(share -> new ShareDTO(
                        share.getShareEmpDeptType(),
                        share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                ? List.of(share.getShareObjUser().getEmpId()) : new ArrayList<>()
                ))
                .toList();
    }

    public static ApproveSbj toApproveSbj(
            ApproveSbjDTO sbjDTO,
            Map<String, Employee> employeeMap,
            ApproveType approveType) {

        Employee sbjUser = null;
        if (sbjDTO.getEmpId() != null) {
            sbjUser = employeeMap.get(sbjDTO.getEmpId());
            if (sbjUser == null) {
                throw new CustomException(ErrorCode.NOT_FOUND_EMP);
            }
        }

        boolean isPll = approveType == ApproveType.PLL || approveType == ApproveType.PLLAGR;

        return ApproveSbj.builder()
                .empDeptType(sbjDTO.getEmpDeptType())
                .sbjUser(sbjUser)
                .isPll(isPll)
                .build();
    }

    public static DocShareObj toDocShareObj(Doc doc, Employee createUser, EmpDeptType empDeptType, Employee employee) {

        return DocShareObj.builder()
                .doc(doc)
                .shareAddUser(createUser)
                .shareEmpDeptType(empDeptType)
                .shareObjUser(employee)
                .build();
    }

}
