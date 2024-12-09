package com.touchdown.perflowbackend.approval.command.application.controller;

import com.touchdown.perflowbackend.approval.command.application.service.ApproveLineCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval/approve-lines")
public class ApproveLineCommandController {

    private final ApproveLineCommandService approveLineCommandService;

    @PostMapping
    public ResponseEntity<SuccessCode> createMyApproveLine() {

        approveLineCommandService.createNewMyApproveLine();

        return ResponseEntity.ok(SuccessCode.MY_APPROVE_LINE_CREATE_SUCCESS);
    }
}
