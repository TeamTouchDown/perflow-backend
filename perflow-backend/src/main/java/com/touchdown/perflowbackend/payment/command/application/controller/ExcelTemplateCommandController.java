package com.touchdown.perflowbackend.payment.command.application.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.payment.command.application.service.ExcelTemplateCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/hr")
public class ExcelTemplateCommandController {

    private final ExcelTemplateCommandService excelTemplateCommandService;

    // 엑셀 템플릿 업로드
    @PostMapping("/payroll-template/upload")
    public ResponseEntity<String> uploadTemplate(@RequestParam("file") MultipartFile file) {

        try {
            // 클라이언트가 수정한 파일 이름 그대로 사용
            String originalFileName = file.getOriginalFilename();

            // 파일 업로드 처리 (파일 이름을 그대로 사용할 경우)
            excelTemplateCommandService.uploadTemplate(file, originalFileName);

            return ResponseEntity.ok(SuccessCode.EXCEL_TEMPLATE_UPLOAD_SUCCESS.getMessage());

        } catch (Exception e) {
            log.error("Excel upload error", e); // 예외 로그 추가
            throw new CustomException(ErrorCode.EXCEL_TEMPLATE_UPLOAD_ERROR);
        }
    }
}
