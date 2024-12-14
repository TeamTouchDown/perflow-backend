package com.touchdown.perflowbackend.approval.command.application.controller;

import com.touchdown.perflowbackend.approval.command.application.dto.ApprovalRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.service.ApprovalService;
import com.touchdown.perflowbackend.approval.command.application.service.DocCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//    @PostMapping("/my-approve-lines")
//    public ResponseEntity<SuccessCode> createMyApproveLine(@RequestBody MyApproveLineCreateRequestDTO request) {
//
//        // todo: 현재 로그인 한 사용자 나중에 넣기
//        String createUserId = "23-MK004";
//        docCommandService.createNewMyApproveLine(request, createUserId);
//
//        return ResponseEntity.ok(SuccessCode.MY_APPROVE_LINE_CREATE_SUCCESS);
//    }
}
