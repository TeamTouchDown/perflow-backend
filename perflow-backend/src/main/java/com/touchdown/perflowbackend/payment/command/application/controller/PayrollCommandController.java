package com.touchdown.perflowbackend.payment.command.application.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.payment.command.application.service.PayrollCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class PayrollCommandController {

    private final PayrollCommandService payrollCommandService;

    @PutMapping("/payrolls/{payrollId}/update")
    public ResponseEntity<String> updatePayroll(
            @PathVariable Long payrollId,
            @RequestParam("file") MultipartFile file) {

        try {
            // 엑셀 파일 처리 서비스 호출
            payrollCommandService.updatePayrollFromExcel(payrollId, file);

            // 성공적인 처리 후 응답
            return ResponseEntity.ok(SuccessCode.PAYROLL_UPLOAD_SUCCESS.getMessage());

        } catch (IOException e) {
            // 파일 처리 중 오류 발생시
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        } catch (CustomException e) {
            // 사용자 정의 예외 처리 (예: Payroll ID 미존재)
            throw new CustomException(ErrorCode.EXCEL_TEMPLATE_UPLOAD_ERROR);

        }
    }
}
