package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.query.dto.PayrollDetailResponseDTO;
import com.touchdown.perflowbackend.payment.query.dto.PayrollListResponseDTO;
import com.touchdown.perflowbackend.payment.query.service.PayrollQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class PayrollQueryController {

    private final PayrollQueryService payrollQueryService;

    @GetMapping("/payrolls/{payrollId}/download")
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

    @GetMapping("/payrolls")
    public ResponseEntity<PayrollListResponseDTO> getPayrolls(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "12") int size
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        PayrollListResponseDTO response = payrollQueryService.getPayrolls(pageable);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/payrolls/{payrollId}")
    public ResponseEntity<PayrollDetailResponseDTO> getPayroll(@PathVariable Long payrollId) {

        PayrollDetailResponseDTO response = payrollQueryService.getPayroll(payrollId);

        return ResponseEntity.ok(response);

    }
}