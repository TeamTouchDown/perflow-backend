package com.touchdown.perflowbackend.approval.query.controller;

import com.touchdown.perflowbackend.approval.query.dto.TemplateDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.TemplateListResponseDTO;
import com.touchdown.perflowbackend.approval.query.service.TemplateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval/templates")
public class TemplateQueryController {

    private final TemplateQueryService templateQueryService;

    // 서식 목록 조회
    @GetMapping
    public ResponseEntity<Page<TemplateListResponseDTO>> readTemplates(Pageable pageable) {

        return ResponseEntity.ok(templateQueryService.getTemplates(pageable));

    }

    // 서식 상세 조회
    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateDetailResponseDTO> readOne(@PathVariable Long templateId) {

        return ResponseEntity.ok(templateQueryService.getOne(templateId));
    }
}
