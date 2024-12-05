package com.touchdown.perflowbackend.approval.query.controller;

import com.touchdown.perflowbackend.approval.query.dto.TemplateListResponseDTO;
import com.touchdown.perflowbackend.approval.query.service.TemplateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/approval")
public class TemplateQueryController {

    private final TemplateQueryService templateQueryService;

    @GetMapping("/templates")
    public ResponseEntity<Page<TemplateListResponseDTO>> showTemplates(Pageable pageable) {


        return ResponseEntity.ok(templateQueryService.getTemplates(pageable));

    }
}
