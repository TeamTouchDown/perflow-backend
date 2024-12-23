package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.approval.command.mapper.DocMapper;
import com.touchdown.perflowbackend.approval.query.dto.*;
import com.touchdown.perflowbackend.approval.query.repository.ApproveLineQueryRepository;
import com.touchdown.perflowbackend.approval.query.repository.ApproveSbjQueryRepository;
import com.touchdown.perflowbackend.approval.query.repository.DocFieldQueryRepository;
import com.touchdown.perflowbackend.approval.query.repository.DocQueryRepository;
import com.touchdown.perflowbackend.approval.query.specification.ApproveSbjSpecification;
import com.touchdown.perflowbackend.approval.query.specification.DocSpecification;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocQueryService {

    private final ApproveLineQueryRepository approveLineQueryRepository;
    private final DocQueryRepository docQueryRepository;
    private final DocFieldQueryRepository docFieldQueryRepository;
    private final ApproveSbjQueryRepository approveSbjQueryRepository;

    // 나의 결재선 목록 조회
    @Transactional(readOnly = true)
    public Page<MyApproveLineGroupResponseDTO> getMyApproveLineList(Pageable pageable, String createUserId
    ) {

        return approveLineQueryRepository.findAllMyApproveLines(pageable, createUserId);
    }

    // 나의 결재선 상세 조회
    @Transactional(readOnly = true)
    public MyApproveLineDetailResponseDTO getOneMyApproveLine(Long groupId) {

        List<ApproveLine> lines = approveLineQueryRepository.findByGroupId(groupId);

        // 첫 번째 결재선에서 결재선 설명, 이름, groupId 가져오기
        ApproveLine firstLine = lines.get(0);

        if (lines.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_MY_APPROVE_LINE);
        }

        return DocMapper.toMyApproveLineDetailResponseDTO(lines, firstLine);
    }

    // 수신함 문서 목록 조회
    @Transactional(readOnly = true)
    public Page<InboxDocListResponseDTO> getInboxDocList(Pageable pageable, String empId, Long deptId, Integer positionLevel) {

        return docQueryRepository.findInboxDocs(pageable, empId, deptId, positionLevel);
    }

    // 수신함 문서 상세 조회
    @Transactional(readOnly = true)
    public InboxDocDetailResponseDTO getOneInboxDoc(Long docId, String empId) {

        // 문서 조회
        Doc doc = docQueryRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));

        return DocMapper.toInboxDocDetailResponseDTO(doc, empId);
    }

    // 발신함 문서 목록 조회
    public Page<OutboxDocListResponseDTO> getOutboxDocList(Pageable pageable, String empId) {

        return docQueryRepository.findOutBoxDocs(pageable, empId);
    }

    // 발신함 문서 목록 검색
    @Transactional(readOnly = true)
    public Page<OutboxDocListResponseDTO> searchOutboxDocList(
            String title,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable,
            String empId
    ) {

        LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = toDate != null ? toDate.atTime(23, 59, 59) : null;

        Specification<Doc> specification = Specification
                .where(DocSpecification.createdBy(empId)) // 내가 생성한 문서
                .and(DocSpecification.titleContains(title)) // 제목 조건
                .and(DocSpecification.createDateBetween(fromDateTime, toDateTime)); // 날짜 조건

        return docQueryRepository.findAll(specification, pageable)
                .map(doc -> OutboxDocListResponseDTO.builder()
                        .docId(doc.getDocId())
                        .templateId(doc.getTemplate().getTemplateId())
                        .title(doc.getTitle())
                        .createDatetime(doc.getCreateDatetime())
                        .status(doc.getStatus())
                        .build());
    }

    // 발신함 문서 상세 조회
    public OutboxDocDetailResponseDTO getOneOutboxDoc(Long docId, String empId) {

        // 문서 조회
        Doc doc = docQueryRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));

        // 작성자가 아닌 경우
        if (!doc.getCreateUser().getEmpId().equals(empId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

        return DocMapper.toOutboxDocDetailResponseDTO(doc);
    }

    // 대기 문서 목록 조회
    @Transactional(readOnly = true)
    public Page<WaitingDocListResponseDTO> getWaitingDocList(Pageable pageable, String empId) {

        // doc id 페이징 처리
        return docQueryRepository.findWaitingDocsByUser(empId, pageable);
    }

    // 대기 문서 목록 검색
    @Transactional(readOnly = true)
    public Page<WaitingDocListResponseDTO> searchWaitingDocList(
            String title,
            String createUser,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable,
            String empId
    ) {

        // LocalDate -> LocalDatetTime
        LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = toDate != null ? toDate.atTime(23, 59, 59) : null;

        // 동적 검색 조건 생성
        Specification<Doc> specification = Specification
                .where(DocSpecification.titleContains(title))
                .and(DocSpecification.createUserNameContains(createUser))
                .and(DocSpecification.createDateBetween(fromDateTime, toDateTime))
                .and(DocSpecification.hasActiveApproveSbjForUser(empId));

        return docQueryRepository.findAll(specification, pageable)
                .map(doc -> WaitingDocListResponseDTO.builder()
                        .docId(doc.getDocId())
                        .templateId(doc.getTemplate().getTemplateId())
                        .title(doc.getTitle())
                        .createUserName(doc.getCreateUser().getName())  // 작성자 이름
                        .empId(doc.getCreateUser().getEmpId())  // 작성자 id
                        .approveLineId(getUserApproveLineId(doc, empId))
                        .approveSbjId(getUserApproveSbjId(doc, empId))
                        .createDatetime(doc.getCreateDatetime())
                        .build());
    }

    // 대기 문서 상세 조회
    @Transactional(readOnly = true)
    public WaitingDocDetailResponseDTO getOneWaitingDoc(Long docId) {

        // 문서 조회
        Doc doc = docQueryRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));

        return DocMapper.toWaitingDocDetailResponseDTO(doc);
    }

    // 처리 문서 목록 조회
    @Transactional(readOnly = true)
    public Page<ProcessedDocListResponseDTO> getProcessedDocList(Pageable pageable, String empId) {

        return docQueryRepository.findProcessedDocs(pageable, empId);
    }

    // 처리 문서 목록 검색
    @Transactional(readOnly = true)
    public Page<ProcessedDocListResponseDTO> searchProcessedDocList(
            String title,
            String createUser,
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable,
            String empId
    ) {

        LocalDateTime fromDateTime = fromDate != null ? fromDate.atStartOfDay() : null;
        LocalDateTime toDateTime = toDate != null ? toDate.atTime(23, 59, 59) : null;

        Specification<ApproveSbj> specification = Specification
                .where(ApproveSbjSpecification.belongsToUser(empId))    // 현재 사용자의 결재 주체
                .and(ApproveSbjSpecification.hasApprovedOrRejectedStatus()) // APPROVED or REJECTED 상태
                .and(ApproveSbjSpecification.titleContains(title))
                .and(ApproveSbjSpecification.createUserNameContains(createUser))
                .and(ApproveSbjSpecification.createDateBetween(fromDateTime, toDateTime))
                .and(ApproveSbjSpecification.docStatusNotApproved());

        return approveSbjQueryRepository.findAll(specification, pageable)
                .map(sbj -> ProcessedDocListResponseDTO.builder()
                        .docId(sbj.getApproveLine().getDoc().getDocId())
                        .templateId(sbj.getApproveLine().getDoc().getTemplate().getTemplateId())
                        .title(sbj.getApproveLine().getDoc().getTitle())
                        .createUserName(sbj.getApproveLine().getDoc().getCreateUser().getName())
                        .empId(sbj.getSbjUser().getEmpId())
                        .approveLineId(sbj.getApproveLine().getApproveLineId())
                        .approveSbjId(sbj.getApproveSbjId())
                        .createDatetime(sbj.getApproveLine().getDoc().getCreateDatetime())
                        .processDatetime(sbj.getUpdateDatetime()) // 처리 시간
                        .build());
    }

    // 처리 시간 조회
    private LocalDateTime getUserProcessDatetime(Doc doc, String empId) {

        return doc.getApproveLines().stream()
                .flatMap(line -> line.getApproveSbjs().stream())
                .filter(sbj -> sbj.getSbjUser().getEmpId().equals(empId) // 현재 사용자가 결재 주체인지 확인
                        && (sbj.getStatus() == Status.APPROVED || sbj.getStatus() == Status.REJECTED)) // 상태가 APPROVED 또는 REJECTED인지 확인
                .findFirst()
                .map(ApproveSbj::getUpdateDatetime) // update_datetime 반환
                .orElse(null);
    }

    // 처리 문서 상세 조회
    @Transactional(readOnly = true)
    public ProcessedDocDetailResponseDTO getOneProcessedDoc(Long docId) {

        // 문서 조회
        Doc doc = docQueryRepository.findById(docId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));

        return DocMapper.toProcessedDocDetailResponseDTO(doc);
    }

    // 현재 사용자가 속한 결재 주체 id
    private Long getUserApproveSbjId(Doc doc, String empId) {

        return doc.getApproveLines().stream()
                .flatMap(line -> line.getApproveSbjs().stream())
                .filter(sbj -> sbj.getSbjUser().getEmpId().equals(empId))
                .findFirst()
                .map(ApproveSbj::getApproveSbjId)
                .orElse(null);
    }

    // 현재 사용자가 속한 결재선 id
    private Long getUserApproveLineId(Doc doc, String empId) {

        return doc.getApproveLines().stream()
                .filter(line -> line.getApproveSbjs().stream()
                        .anyMatch(sbj -> sbj.getSbjUser().getEmpId().equals(empId)))
                .findFirst()
                .map(ApproveLine::getApproveLineId)
                .orElse(null);
    }
}
