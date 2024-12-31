package com.touchdown.perflowbackend.payment.query.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayDetailResponseDTO;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayListResponseDTO;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayStubDetailDTO;
import com.touchdown.perflowbackend.payment.query.service.SeverancePayQueryService;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SeverancePayQueryController {

    private final SeverancePayQueryService severancePayQueryService;

    @GetMapping("/hr/severance-pay/{severancePayId}/download")
    public ResponseEntity<byte[]> downloadSeverancePay(@PathVariable Long severancePayId) {

        try {
            // 급여 데이터 생성
            byte[] severancePay = severancePayQueryService.generateSeverancePay(severancePayId);

            // 현재 날짜로 연도와 월 추출
            LocalDate now = LocalDate.now();
            String year = String.valueOf(now.getYear()); // 연도
            String month = String.format("%02d", now.getMonthValue()); // 월 (두 자리로 포맷)

            // 파일 이름 생성
            String fileName = "severancePay_" + year + "_" + month + ".xlsx";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                    .body(severancePay);

        } catch (IOException e) {

            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);

        }
    }

    @GetMapping("/hr/severance-pays")
    public ResponseEntity<SeverancePayListResponseDTO> getSeverancePays(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        SeverancePayListResponseDTO response = severancePayQueryService.getSeverancePays(pageable);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/hr/severance-pays/{severancePayId}")
    public ResponseEntity<SeverancePayDetailResponseDTO> getSeverancePay(@PathVariable Long severancePayId) {

        SeverancePayDetailResponseDTO response = severancePayQueryService.getSeverancePay(severancePayId);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/severance-stub")
    public ResponseEntity<SeverancePayStubDetailDTO> getSeverancePayStub() {

        String empId = EmployeeUtil.getEmpId();

        SeverancePayStubDetailDTO response = severancePayQueryService.getSeverancePayStub(empId);

        log.error("Response DTO: {}", response);

        return ResponseEntity.ok(response);

    }

}
