package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeQRCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "WorkAttitude-Attendance-Controller", description = "출퇴근 관련 API")
public class WorkAttitudeQRCommandController {

    private final WorkAttitudeQRCommandService workAttitudeQRCommandService;

    @Operation(summary = "QR 코드 생성", description = "사용자의 empId로 QR 코드를 생성하여 Base64 형식으로 반환합니다.")
    @PostMapping("/emp/qr/generate")
    public ResponseEntity<String> generateQRCode() {
        String qrCodeBase64 = workAttitudeQRCommandService.generateQRCode();
        return ResponseEntity.ok(qrCodeBase64);
    }

    @Operation(summary = "QR 코드 검증", description = "사용자의 empId와 입력된 QR 코드를 검증합니다.")
    @PostMapping("/emp/qr/validate")
    public ResponseEntity<SuccessCode> validateQRCode(@RequestParam String code) {
        workAttitudeQRCommandService.validateQRCode(code);
        return ResponseEntity.ok(SuccessCode.QR_CODE_VALIDATION_SUCCESS);
    }
}
