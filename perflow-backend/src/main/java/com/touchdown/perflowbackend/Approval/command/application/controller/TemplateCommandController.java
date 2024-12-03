package com.touchdown.perflowbackend.Approval.command.application.controller;

import com.touchdown.perflowbackend.Approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.Approval.command.application.dto.TemplateCreateResponseDTO;
import com.touchdown.perflowbackend.Approval.command.application.service.TemplateCommandService;
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
public class TemplateCommandController {

    private final TemplateCommandService templateCommandService;

    // 서식 생성(1) : 서식명, 서식 내용
    @PostMapping("/templates")
    public ResponseEntity<String> createTemplate(@RequestBody TemplateCreateRequestDTO request) {

        // todo: 사원 기능 생기면 수정
        String empId = "23-FN002";

        TemplateCreateResponseDTO response = templateCommandService.createNewTemplate(request, empId);

        return ResponseEntity.ok(SuccessCode.TEMPLATE_CREATE_SUCCESS.getMessage());
    }


    // 서식 생성(2) : 서식 에디터

}
