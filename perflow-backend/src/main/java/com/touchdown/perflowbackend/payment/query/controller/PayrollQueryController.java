package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.query.service.PayrollQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class PayrollQueryController {

    private final PayrollQueryService payrollQueryService;

    @GetMapping("/payrolls/{payrollId}")
    public ResponseEntity<byte[]> downloadPayroll(@PathVariable long payrollId) {

        try {
            // 급여 데이터 생성
            byte[] payroll = payrollQueryService.generatePayroll(payrollId);

            // 현재 날짜로 연도와 월 추출
            LocalDate now = LocalDate.now();
            String year = String.valueOf(now.getYear()); // 연도
            String month = String.format("%02d", now.getMonthValue()); // 월 (두 자리로 포맷)

            // 파일 이름 생성
            String fileName = "payroll_" + year + "_" + month + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(payroll);

        } catch (IOException e) {

            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        }
    }
}