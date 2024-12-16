package com.touchdown.perflowbackend.approval.command.application.controller;

import com.touchdown.perflowbackend.approval.command.application.dto.*;
import com.touchdown.perflowbackend.approval.command.application.service.ApprovalService;
import com.touchdown.perflowbackend.approval.command.application.service.DocCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import io.netty.util.concurrent.SucceededFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class DocCommandController {

    private final DocCommandService docCommandService;
    private final ApprovalService approvalService;

    // 결재 문서 생성
    @PostMapping("/docs")
    public ResponseEntity<SuccessCode> createDoc(
            @RequestBody DocCreateRequestDTO request
    ) {

        // todo: 현재 로그인 한 사용자 나중에 넣기
        String createUserId = "23-MK004";

        docCommandService.createNewDoc(request, createUserId);

        return ResponseEntity.ok(SuccessCode.DOC_CREATE_SUCCESS);
    }

    // 문서 단일 결재
    @PutMapping("/docs")
    public ResponseEntity<SuccessCode> approveDoc(@RequestBody ApprovalRequestDTO request) {

        approvalService.processApproval(request);

        return ResponseEntity.ok(SuccessCode.DOC_APPROVE_SUCCESS);
    }

    // 문서 일괄 승인

    // 나의 결재선 생성
    @PostMapping("/my-approve-lines")
    public ResponseEntity<SuccessCode> createMyApproveLine(@RequestBody MyApproveLineCreateRequestDTO request) {

        // todo: 현재 로그인 한 사용자 나중에 넣기
        String createUserId = "23-MK004";
        docCommandService.createNewMyApproveLine(request, createUserId);

        return ResponseEntity.ok(SuccessCode.MY_APPROVE_LINE_CREATE_SUCCESS);
    }

    // 나의 결재선 수정
    @PutMapping("/my-approve-lines/{groupId}")
    public ResponseEntity<SuccessCode> updateMyApproveLine(
            @PathVariable Long groupId,
            @RequestBody MyApproveLineUpdateRequestDTO request) {

        String updateUserId = "23-MK004";

        docCommandService.updateMyApproveLine(groupId, request, updateUserId);

        return ResponseEntity.ok(SuccessCode.MY_APPROVE_LINE_UPDATE_SUCCESS);
    }

    // 나의 결재선 단일 삭제
    @DeleteMapping("/my-approve-lines/{groupId}")
    public ResponseEntity<SuccessCode> deleteMyApproveLine(@PathVariable Long groupId) {

        String deleteUserId = "23-MK004";

        docCommandService.deleteMyApproveLine(groupId, deleteUserId);

        return ResponseEntity.ok(SuccessCode.MY_APPROVE_LINE_DELETE_SUCCESS);
    }

    // 나의 결재선 일괄 삭제
    @DeleteMapping("/my-approve-lines")
    public ResponseEntity<SuccessCode> deleteMyApproveLines(@RequestBody BulkDeleteRequestDTO request) {

        String deleteUserId = "23-MK004";

        docCommandService.deleteMyApproveLines(request, deleteUserId);

        return ResponseEntity.ok(SuccessCode.MY_APPROVE_LINE_DELETE_SUCCESS);
    }

}
