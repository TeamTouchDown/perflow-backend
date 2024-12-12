package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.query.dto.*;
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
import java.util.List;

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

    // 급여대장 검색
    @GetMapping("/payrolls/search")
    public ResponseEntity<PayrollDetailResponseDTO> searchPayroll(

            @RequestParam(required = false) String empName,
            @RequestParam(required = false) String empId,
            @RequestParam(required = false) String deptName

    ) {

        PayrollDetailResponseDTO response = payrollQueryService.searchPayroll(empName, empId, deptName);

        return ResponseEntity.ok(response);

    }

    // 해당 월의 3년간 급여 데이터 조회
    @GetMapping("/payrolls/chart/last-three-years-by-latest-month")
    public ResponseEntity<List<PayrollChartDTO>> getPayrollsByMonthAndThreeYears() {

        List<PayrollChartDTO> payrolls = payrollQueryService.getPayrollsByMonthAndThreeYears();
        return ResponseEntity.ok(payrolls);

    }

    // 3개월간 급여 데이터 조회
    @GetMapping("/payrolls/chart/last-three-months")
    public ResponseEntity<List<PayrollChartDTO>> getLastThreeMonthsPayrolls() {

        List<PayrollChartDTO> payrolls = payrollQueryService.getLastThreeMonthsPayrolls();
        return ResponseEntity.ok(payrolls);

    }

    // 3년간 급여 데이터 조회
    @GetMapping("/payrolls/chart/last-three-years")
    public ResponseEntity<List<PayrollChartDTO>> getLastThreeYearsPayrolls() {

        List<PayrollChartDTO> payrolls = payrollQueryService.getLastThreeYearsPayrolls();
        return ResponseEntity.ok(payrolls);

    }

    @GetMapping("/pay-stub/{empId}")
    public ResponseEntity<PayStubDTO> getPayStub(@PathVariable String empId) {

        PayStubDTO response = payrollQueryService.getPayStub(empId);

        return ResponseEntity.ok(response);

    }
}