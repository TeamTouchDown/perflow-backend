package com.touchdown.perflowbackend.approval.command.application.controller;

import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateResponseDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.TemplateUpdateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.TemplatesDeleteRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.service.TemplateCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval/templates")
public class TemplateCommandController {

    private final TemplateCommandService templateCommandService;

    @PostMapping
    public ResponseEntity<TemplateCreateResponseDTO> createTemplate(
            @RequestBody TemplateCreateRequestDTO request
    ) {

        // todo: 사원 기능 생기면 수정
        String empId = "23-FN002";

        TemplateCreateResponseDTO response = templateCommandService.createNewTemplate(request, empId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<SuccessCode> updateTemplate(
            @RequestBody TemplateUpdateRequestDTO request,
            @PathVariable Long templateId
    ) {

        templateCommandService.modifyTemplate(request, templateId);

        return ResponseEntity.ok(SuccessCode.TEMPLATE_UPDATE_SUCCESS);
    }

    @DeleteMapping("/{templateId}")
    public ResponseEntity<SuccessCode> deleteTemplate(@PathVariable Long templateId) {

        templateCommandService.removeTemplate(templateId);

        return ResponseEntity.ok(SuccessCode.TEMPLATE_DELETE_SUCCESS);
    }

    @DeleteMapping
    public ResponseEntity<SuccessCode> deleteTemplates(@RequestBody TemplatesDeleteRequestDTO request) {

        templateCommandService.removeTemplates(request);

        return ResponseEntity.ok(SuccessCode.TEMPLATE_DELETE_SUCCESS);
    }
}
