package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApprovalRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.BulkApproveRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveLineCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveSbjCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.approval.command.infrastructure.repository.JpaApproveSbjCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalService {

    private final DocCommandRepository docCommandRepository;
    private final ApproveLineCommandRepository approveLineCommandRepository;
    private final ApproveSbjCommandRepository approveSbjCommandRepository;
    private final JpaApproveSbjCommandRepository jpaApproveSbjCommandRepository;

    // 문서 결재(승인 or 반려)
    @Transactional
    public void processApproval(ApprovalRequestDTO request) {

        // todo: 로그인 기능 이후 수정하기
        String LoginId = EmployeeUtil.getEmpId();

        // 문서 조회
        Doc doc = findDocById(request.getDocId());

        // 결재 주체 조회
        ApproveSbj approveSbj = findApproveSbjById(
                request.getDocId(),
                request.getEmpDeptType(),
                request.getApproveLineId(),
                request.getApproveSbjId()
        );

        // 결재 주체 상태 변경
        updateApproveSbjStatus(approveSbj, request.getStatus(), request.getComment());

        // 결재 방식 처리
        handleApproval(doc, approveSbj);

        // 문서 상태 업데이트
        updateDocStatus(approveSbj.getApproveLine().getDoc());
    }

    // 문서 일괄 승인
    @Transactional
    public void processBulkApproval(BulkApproveRequestDTO request) {

        for (ApprovalRequestDTO approval : request.getApprovals()) {
            try {
                processApproval(approval);
            } catch (CustomException e) {
                // 예외 발생 시 다음 문서 승인 처리
                log.error("문서 {} 승인이 {} 오류로 실패함", approval.getDocId(), e.getMessage());
            }
        }
    }

    // 결재선 상태 업데이트
    private void updateApproveLineStatus(ApproveLine line) {

        List<ApproveSbj> sbjs = line.getApproveSbjs();

        if (isRejectedAny(sbjs)) {
            line.updateStatus(Status.REJECTED);
        } else if (isApprovedAll(sbjs)) {
            line.updateStatus(Status.APPROVED);
        } else {
            line.updateStatus(Status.PENDING);
        }
        approveLineCommandRepository.save(line);
    }

    // 문서 상태 업데이트
    private void updateDocStatus(Doc doc) {

        // 모든 결재선 상태를 최신으로 갱신
        doc.getApproveLines().forEach(this::updateApproveLineStatus);

        List<ApproveLine> allLines = doc.getApproveLines();

        if (allLines.stream().anyMatch(line -> line.getStatus() == Status.REJECTED)) {
            doc.updateStatus(Status.REJECTED);
        } else if (allLines.stream().allMatch(line -> line.getStatus() == Status.APPROVED)) {
            doc.updateStatus(Status.APPROVED);  // 모든 결재선이 APPROVED 여야 문서를 APPROVED 로
        } else {
            doc.updateStatus(Status.ACTIVATED);
        }

        docCommandRepository.save(doc);

    }

    // 결재 방식 처리
    private void handleApproval(Doc doc, ApproveSbj approveSbj) {

        ApproveType approveType = approveSbj.getApproveLine().getApproveType();

        // 결재선 상태 업데이트
        updateApproveLineStatus(approveSbj.getApproveLine());

        switch(approveType) {

            // 동의, 합의
            case SEQ, AGR:
                handleSeqApproval(doc, approveSbj);
                break;
            // 병렬, 병렬합의
            case PLL, PLLAGR:
                handlePllApproval(doc, approveSbj);
                break;
            // 참조
            case CC:
                break;
            // 전결
            case ARB:
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_APPROVE_TYPE);
        }
    }

    // 병렬, 병렬 합의 처리
    private void handlePllApproval(Doc doc, ApproveSbj approveSbj) {

        ApproveLine currentLine = approveSbj.getApproveLine();

        // 현재 결재선 상태 업데이트
        updateApproveLineStatus(currentLine);

        if (currentLine.getStatus() == Status.REJECTED) {
            // 현재 결재선 중 하나라도 반려되면 문서를 REJECTED 로
            doc.updateStatus(Status.REJECTED);
            docCommandRepository.save(doc);
        } else if (currentLine.getStatus() == Status.APPROVED) {
            // 현재 결재선의 모든 주체가 승인하면 다음 결재선으로
            moveToNextApproveLine(doc, approveSbj.getApproveLine());
        }
    }

    // 다음 결재선으로 이동
    private void moveToNextApproveLine(Doc doc, ApproveLine currentLine) {

        Optional<ApproveLine> nextLineOpt = approveLineCommandRepository.findNextApproveLineAsc(
                doc.getDocId(),
                currentLine.getApproveLineOrder()
        );

        if (nextLineOpt.isPresent()) {
            ApproveLine nextLine = nextLineOpt.get();
            nextLine.getApproveSbjs().forEach(sbj -> sbj.updateStatus(Status.ACTIVATED));
            approveLineCommandRepository.save(nextLine);

        } else {
            // 다음 결재선이 없으면 문서를 APPROVED 상태로 변경
            doc.updateStatus(Status.APPROVED);
            docCommandRepository.save(doc);
        }
    }

    // 동의, 합의 처리
    private void handleSeqApproval(Doc doc, ApproveSbj approveSbj) {

        // 현재 결재선의 모든 결재 주체의 상태
        List<ApproveSbj> subjects = approveSbj.getApproveLine().getApproveSbjs();

        // 현재 결재 주체가 속한 결재선의 순서를 기준으로 다음 결재 주체 찾기
        Optional<ApproveSbj> nextSbjOpt = subjects.stream()
                .filter(sbj -> sbj.getApproveLine().getApproveLineOrder() > approveSbj.getApproveLine().getApproveLineOrder())
                .min(Comparator.comparing(sbj -> sbj.getApproveLine().getApproveLineOrder()));

        if (nextSbjOpt.isPresent()) {
            // 다음 결재 주체가 있다면 상태를 PENDING 으로 변경
            ApproveSbj nextSbj = nextSbjOpt.get();
            nextSbj.updateStatus(Status.ACTIVATED);
            jpaApproveSbjCommandRepository.save(nextSbj);
        } else {
            // 다음 결재 주체가 없다면 다음 결재선으로 이동
            moveToNextApproveLine(doc, approveSbj.getApproveLine());
        }
    }

    // 결제 주체 상태 변경
    private void updateApproveSbjStatus(ApproveSbj approveSbj, Status status, String comment) {

        approveSbj.updateStatus(status);

        // 승인, 반려 의견이 있으면 저장
        if (comment != null) {
            approveSbj.updateComment(comment);
        }

        jpaApproveSbjCommandRepository.save(approveSbj);
    }

    // 결재 주체들이 모두 승인
    private boolean isApprovedAll(List<ApproveSbj> subjects) {

        return subjects.stream().allMatch(sbj -> sbj.getStatus() == Status.APPROVED);
    }

    // 결재 주체들 중 한 명이라도 반려
    private boolean isRejectedAny(List<ApproveSbj> subjects) {

        return subjects.stream().anyMatch(sbj -> sbj.getStatus() == Status.REJECTED);
    }

    private Doc findDocById(Long docId) {

        return docCommandRepository.findById(docId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));
    }

    private ApproveSbj findApproveSbjById(Long docId, EmpDeptType empDeptType, Long approveLineId, Long approveSbjId) {

        return approveSbjCommandRepository.findByDocIdAndApproveSbjIdAndType(docId, empDeptType, approveLineId, approveSbjId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));
    }

}
