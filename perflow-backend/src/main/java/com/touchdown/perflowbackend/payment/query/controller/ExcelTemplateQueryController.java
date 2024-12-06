package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.query.service.ExcelTemplateQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/hr")
@RequiredArgsConstructor
public class ExcelTemplateQueryController {

    private final ExcelTemplateQueryService excelTemplateQueryService;

    // 엑셀 템플릿 다운로드
    @GetMapping("/payroll-template/download")
    public ResponseEntity<byte[]> downloadTemplate() {

        try {

            byte[] template = excelTemplateQueryService.generatePayrollTemplate();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payroll_template.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(template);

        } catch (IOException e) {

            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        }
    }
}
