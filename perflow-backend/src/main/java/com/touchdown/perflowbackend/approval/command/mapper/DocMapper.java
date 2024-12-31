package com.touchdown.perflowbackend.approval.command.mapper;

import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;

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
//                .empId(doc.getCreateUser().getEmpId())
//                .approveLineId(doc.getApproveLines().getApproveLIneId())
//                .approveSbjId(doc.getApproveLines().getApproveSbj.getApproveSbjId())
                .createDatetime(doc.getCreateDatetime())
                .build();
    }

    // 대기 문서 상세 조회 시
    public static WaitingDocDetailResponseDTO toWaitingDocDetailResponseDTO(Doc doc) {
        return WaitingDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .title(doc.getTitle())
                .createUserName(doc.getCreateUser().getName())
                .createUserDept(doc.getCreateUser().getDept().getName())
                .createUserPosition(doc.getCreateUser().getPosition().getName())
                .createDatetime(doc.getCreateDatetime())
                .fields(mapFields(doc.getDocFields())) // 필드 데이터 매핑
                .approveLines(mapWaitingApproveLines(doc.getApproveLines())) // 결재선 매핑
                .shares(mapWaitingShares(doc.getShares())) // 공유 설정 매핑
                .build();
    }

    // 처리 문서 상세 조회 시
    public static ProcessedDocDetailResponseDTO toProcessedDocDetailResponseDTO(Doc doc) {

        return ProcessedDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .createUserName(doc.getCreateUser().getName())
                .createUserDept(doc.getCreateUser().getDept().getName())
                .createUserPosition(doc.getCreateUser().getPosition().getName())
                .createDatetime(doc.getCreateDatetime())
                .title(doc.getTitle())
                .fields(mapFields(doc.getDocFields())) // 필드 데이터 매핑
                .approveLines(mapProcessedApproveLines(doc.getApproveLines())) // 결재선 매핑
                .shares(mapProcessedShares(doc.getShares())) // 공유 설정 매핑
                .build();
    }

    private static List<ProcessedDocShareDTO> mapProcessedShares(List<DocShareObj> shares) {

        return shares.stream()
                .map(share -> ProcessedDocShareDTO.builder()
                        .shareEmpDeptType(share.getShareEmpDeptType()) // 공유 대상 타입
                        .empIds(
                                share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                        ? List.of(share.getShareObjUser().getEmpId())
                                        : List.of()
                        ) // 사원 ID
                        .empNames(
                                share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                        ? List.of(share.getShareObjUser().getName())
                                        : List.of()
                        ) // 사원 이름
                        .build())
                .collect(Collectors.toList());
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

    // 수신함 문서 상세 조회 시
    public static InboxDocDetailResponseDTO toInboxDocDetailResponseDTO(Doc doc, String empId) {

        String myStatus = getMyStatus(doc, empId);
        String docStatus = getDocStatus(doc.getStatus());
        
        return InboxDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .createUserName(doc.getCreateUser().getName())
                .createUserDept(doc.getCreateUser().getDept().getName())
                .createUserPosition(doc.getCreateUser().getPosition().getName())
                .createDatetime(doc.getCreateDatetime())
                .title(doc.getTitle())
                .fields(mapFields(doc.getDocFields())) // 문서 필드 매핑
                .approveLines(mapInboxApproveLines(doc.getApproveLines())) // 결재선 매핑
                .shares(mapInboxShares(doc.getShares())) // 공유 설정 매핑
                .myStatus(myStatus) // 내 결재 상태
                .docStatus(docStatus) // 문서 상태
                .build();
    }

    private static List<InboxDocShareDTO> mapInboxShares(List<DocShareObj> shares) {

        return shares.stream()
                .map(share -> InboxDocShareDTO.builder()
                        .shareEmpDeptType(share.getShareEmpDeptType())
                        .empIds(
                                share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                        ? List.of(share.getShareObjUser().getEmpId())
                                        : List.of()
                        )
                        .empNames(
                                share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                        ? List.of(share.getShareObjUser().getName())
                                        : List.of()
                        )
                        .build())
                .collect(Collectors.toList());
    }

    private static List<InboxDocApproveLineDTO> mapInboxApproveLines(List<ApproveLine> approveLines) {

        return approveLines.stream()
                .map(line -> InboxDocApproveLineDTO.builder()
                        .approveLineId(line.getApproveLineId())
                        .groupId(line.getGroupId())
                        .approveType(line.getApproveType())
                        .approveLineOrder(line.getApproveLineOrder())
                        .pllGroupId(line.getPllGroupId())
                        .approveTemplateType(line.getApproveTemplateType())
                        .approveSbjs(mapInboxApproveSubjects(line.getApproveSbjs())) // 결재 주체 매핑
                        .build())
                .collect(Collectors.toList());
    }

    private static List<InboxDocApproveSbjDTO> mapInboxApproveSubjects(List<ApproveSbj> approveSbjs) {

        return approveSbjs.stream()
                .map(sbj -> InboxDocApproveSbjDTO.builder()
                        .empDeptType(sbj.getEmpDeptType())
                        .empId(sbj.getSbjUser() != null ? sbj.getSbjUser().getEmpId() : null)
                        .empName(sbj.getSbjUser() != null ? sbj.getSbjUser().getName() : null)
                        .approveLineId(sbj.getApproveLine().getApproveLineId())
                        .approveSbjId(sbj.getApproveSbjId())
                        .status(sbj.getStatus())
                        .comment(sbj.getComment())
                        .build())
                .collect(Collectors.toList());
    }

    // 문서 상태 표시
    private static String getDocStatus(Status status) {
        return switch (status) {
            case APPROVED -> "승인";
            case REJECTED -> "반려";
            case ACTIVATED -> "진행";
            default -> "알 수 없음";
        };
    }

    // 내 결재 상태
    private static String getMyStatus(Doc doc, String empId) {
        for (ApproveLine line : doc.getApproveLines()) {
            for (ApproveSbj sbj : line.getApproveSbjs()) {
                if (sbj.getSbjUser() != null && sbj.getSbjUser().getEmpId().equals(empId)) {
                    return myStatusToKor(sbj.getStatus()); // 예: "ACTIVATED", "APPROVED"
                }
            }
        }
        return "내가 결재선에 없음";  // 내가 결재 주체에 포함되지 않음
    }

    private static String myStatusToKor(Status status) {

        return switch (status) {
            case APPROVED -> "승인";
            case REJECTED -> "반려";
            case ACTIVATED -> "진행";
            default -> "알 수 없음";
        };
    }

    // 발신함 문서 상세 조회 시
    public static OutboxDocDetailResponseDTO toOutboxDocDetailResponseDTO(Doc doc) {

        return OutboxDocDetailResponseDTO.builder()
                .docId(doc.getDocId())
                .createUserName(doc.getCreateUser().getName())
                .createUserDept(doc.getCreateUser().getDept().getName())
                .createUserPosition(doc.getCreateUser().getPosition().getName())
                .createDatetime(doc.getCreateDatetime())
                .title(doc.getTitle())
                .fields(mapFields(doc.getDocFields())) // 필드 데이터 매핑
                .approveLines(mapOutboxApproveLines(doc.getApproveLines())) // 결재선 정보 매핑
                .shares(mapOutboxShares(doc.getShares())) // 공유 설정 정보 매핑
                .status(getDocStatus(doc.getStatus())) // 문서 상태 매핑
                .build();
    }

    private static List<OutboxDocApproveLineDTO> mapOutboxApproveLines(List<ApproveLine> approveLines) {

        return approveLines.stream()
                .map(line -> OutboxDocApproveLineDTO.builder()
                        .approveLineId(line.getApproveLineId())
                        .groupId(line.getGroupId())
                        .approveType(line.getApproveType().toString())
                        .approveLineOrder(line.getApproveLineOrder())
                        .status(line.getStatus().toString())
                        .approveSbjs(mapOutboxApproveSubjects(line.getApproveSbjs()))
                        .build())
                .collect(Collectors.toList());
    }

    private static List<OutboxDocApproveSbjDTO> mapOutboxApproveSubjects(List<ApproveSbj> approveSbjs) {

        return approveSbjs.stream()
                .map(sbj -> OutboxDocApproveSbjDTO.builder()
                        .empId(sbj.getSbjUser().getEmpId())
                        .empName(sbj.getSbjUser().getName())
                        .status(sbj.getStatus().toString())
                        .comment(sbj.getComment())
                        .build())
                .collect(Collectors.toList());
    }

    private static List<OutboxDocShareDTO> mapOutboxShares(List<DocShareObj> shares) {
        return shares.stream()
                .map(share -> OutboxDocShareDTO.builder()
                        .shareEmpDeptType(share.getShareEmpDeptType())
                        .empIds(
                                share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                        ? List.of(share.getShareObjUser().getEmpId())
                                        : List.of()
                        )
                        .empNames(
                                share.getShareEmpDeptType() == EmpDeptType.EMPLOYEE && share.getShareObjUser() != null
                                        ? List.of(share.getShareObjUser().getName())
                                        : List.of()
                        )
                        .build())
                .collect(Collectors.toList());
    }
}
