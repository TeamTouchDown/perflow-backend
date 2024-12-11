package com.touchdown.perflowbackend.approval.command.application.service;

import com.touchdown.perflowbackend.approval.command.application.dto.ApprovalRequestDTO;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.*;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveLineCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveSbjCommandRepository;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final DocCommandRepository docCommandRepository;
    private final ApproveLineCommandRepository approveLineCommandRepository;
    private final ApproveSbjCommandRepository approveSbjCommandRepository;

    public void processApproval(ApprovalRequestDTO request) {

        // todo: 로그인 기능 이후 수정하기
        String LoginId = "23-HR001";

        // 문서 조회
        Doc doc = findDocById(request.getDocId());

        // 결재 주체 조회
        ApproveSbj approveSbj = findApproveSbjById(request.getDocId(), LoginId, request.getSbjType());

        // 결재 주체 상태 변경
        updateApproveSbjStatus(approveSbj, request.getStatus());

        // 결재 방식 처리
        handleApproval(doc, approveSbj);

        // 문서 상태 업데이트
        updateDocStatus(approveSbj.getApproveLine().getDoc());
    }

    // 문서 상태 업데이트
    private void updateDocStatus(Doc doc) {

        List<ApproveSbj> allSubjects = doc.getApproveLines().stream()
                .flatMap(line -> line.getApproveSubjects().stream())
                .toList();

        if (isRejectedAny(allSubjects)) {
            doc.updateStatus(Status.REJECTED);
        } else if (isApprovedAll(allSubjects)) {
            doc.updateStatus(Status.APPROVED);
        }

        docCommandRepository.save(doc);

    }

    // 결재 방식 처리
    private void handleApproval(Doc doc, ApproveSbj approveSbj) {

        switch(approveSbj.getApproveLine().getApproveType()) {

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

        // 현재 결재선의 모든 결재 주체의 상태
        List<ApproveSbj> subjects = approveSbj.getApproveLine().getApproveSubjects();

        if (isRejectedAny(subjects)) {
            doc.updateStatus(Status.REJECTED);
            docCommandRepository.save(doc);
        } else if (isApprovedAll(subjects)) {
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

            // 다음 결재선의 모든 결재 주체를 PENDING 으로
            nextLine.getApproveSubjects().forEach(sbj -> {
                sbj.updateStatus(Status.PENDING);
                approveSbjCommandRepository.save(sbj);
            });
        } else {
            // 다음 결재선이 없으면 문서를 APPROVED 상태로 변경
            doc.updateStatus(Status.APPROVED);
            docCommandRepository.save(doc);
        }
    }

    // 동의, 합의 처리
    private void handleSeqApproval(Doc doc, ApproveSbj approveSbj) {

        // 현재 결재선의 모든 결재 주체의 상태
        List<ApproveSbj> subjects = approveSbj.getApproveLine().getApproveSubjects();

        // 현재 결재 주체의 순서보다 높은 주체를 필터링
        Optional<ApproveSbj> nextSbjOpt = subjects.stream()
                .filter(sbj -> sbj.getApproveLine().getApproveLineOrder() > approveSbj.getApproveLine().getApproveLineOrder()) // ApproveLine 참조
                .sorted(Comparator.comparingInt(sbj -> sbj.getApproveLine().getApproveLineOrder()))
                .findFirst();

        if (nextSbjOpt.isPresent()) {
            // 다음 결재 주체가 있다면 상태를 PENDING 으로 변경
            ApproveSbj nextSbj = nextSbjOpt.get();
            nextSbj.updateStatus(Status.PENDING);
            approveSbjCommandRepository.save(nextSbj);
        } else {
            // 다음 결재 주체가 없다면 다음 결재선으로 이동
            moveToNextApproveLine(doc, approveSbj.getApproveLine());
        }
    }

    // 결제 주체 상태 변경
    private void updateApproveSbjStatus(ApproveSbj approveSbj, Status status) {

        approveSbj.updateStatus(status);

        approveSbjCommandRepository.save(approveSbj);
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

    private ApproveSbj findApproveSbjById(Long docId, String approveSbjId, SbjType sbjType) {

        return approveSbjCommandRepository.findByDocIdAndApproveSbjIdAndType(docId, approveSbjId, sbjType)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));
    }
}
