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
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/hr")
@RequiredArgsConstructor
public class ExcelTemplateQueryController {

    private final ExcelTemplateQueryService excelTemplateQueryService;

    // 엑셀 템플릿 다운로드
    @GetMapping("/payroll-template/download")
    public ResponseEntity<byte[]> downloadTemplate() {

        try {
            // 엑셀 템플릿을 바이트 배열로 생성
            byte[] template = excelTemplateQueryService.generatePayrollTemplate();

            // 현재 날짜로 연도와 월 추출
            LocalDate now = LocalDate.now();
            String year = String.valueOf(now.getYear()); // 연도
            String month = String.format("%02d", now.getMonthValue()); // 월 (두 자리로 포맷)

            // 파일 이름 생성
            String fileName = "payroll_" + year + "_" + month + ".xlsx";

            // 응답 헤더 설정 및 Excel 파일 반환
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(template);

        } catch (IOException e) {
            // 예외 발생 시 처리
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        }
    }
}

