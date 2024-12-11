package com.touchdown.perflowbackend.approval.command.application.controller;

import com.touchdown.perflowbackend.approval.command.application.dto.DocCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.MyApproveLineCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.service.DocCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class DocCommandController {

    private final DocCommandService docCommandService;

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

    // 나의 결재선 생성
    @PostMapping("/my-approve-lines")
    public ResponseEntity<SuccessCode> createMyApproveLine(@RequestBody MyApproveLineCreateRequestDTO request) {

        // todo: 현재 로그인 한 사용자 나중에 넣기
        String createUserId = "23-MK004";
        docCommandService.createNewMyApproveLine(request, createUserId);

        return ResponseEntity.ok(SuccessCode.MY_APPROVE_LINE_CREATE_SUCCESS);
    }
}
