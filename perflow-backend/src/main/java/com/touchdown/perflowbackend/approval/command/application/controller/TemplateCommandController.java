package com.touchdown.perflowbackend.approval.command.application.controller;

import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateRequestDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.TemplateCreateResponseDTO;
import com.touchdown.perflowbackend.approval.command.application.service.TemplateCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class TemplateCommandController {

    private final TemplateCommandService templateCommandService;

    @PostMapping("/templates")
    public ResponseEntity<TemplateCreateResponseDTO> createTemplate(
            @RequestBody TemplateCreateRequestDTO request
    ) {

        // todo: 사원 기능 생기면 수정
        String empId = "23-FN002";

        TemplateCreateResponseDTO response = templateCommandService.createNewTemplate(request, empId);

        return ResponseEntity.ok(
                new TemplateCreateResponseDTO(response.getTemplateId())
        );
    }

}
