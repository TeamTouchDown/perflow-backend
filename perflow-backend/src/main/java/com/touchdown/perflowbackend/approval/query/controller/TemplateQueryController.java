package com.touchdown.perflowbackend.approval.query.controller;

import com.touchdown.perflowbackend.approval.query.dto.TemplateDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.TemplateListResponseDTO;
import com.touchdown.perflowbackend.approval.query.service.TemplateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval/templates")
public class TemplateQueryController {

    private final TemplateQueryService templateQueryService;

    @GetMapping
    public ResponseEntity<Page<TemplateListResponseDTO>> readTemplates(Pageable pageable) {

        return ResponseEntity.ok(templateQueryService.getTemplates(pageable));

    }

    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateDetailResponseDTO> readOne(@PathVariable Long templateId) {

        return ResponseEntity.ok(templateQueryService.getOne(templateId));
    }
}
