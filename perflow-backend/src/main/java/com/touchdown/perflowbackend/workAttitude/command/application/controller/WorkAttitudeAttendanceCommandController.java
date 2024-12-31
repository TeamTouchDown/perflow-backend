package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeAttendanceCommandService;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeQRCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttitude-Attendance-Controller", description = "출퇴근 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emp/attendances")
public class WorkAttitudeAttendanceCommandController {private final WorkAttitudeAttendanceCommandService attendanceCommandService;

    @Operation(summary = "출근용 QR 코드 생성", description = "출근을 위한 QR 코드를 생성합니다.")
    @PostMapping("/check-in/qr/generate")
    public ResponseEntity<String> generateCheckInQRCode() {
        String qrCodeBase64 = attendanceCommandService.generateQRCodeForCheckIn();
        return ResponseEntity.ok(qrCodeBase64);
    }

    @Operation(summary = "출근 처리", description = "QR 코드 검증 후 출근을 처리합니다.")
    @PostMapping("/check-in")
    public ResponseEntity<SuccessCode> validateAndCheckIn(@RequestParam String qrCode) {
        attendanceCommandService.validateAndCheckIn(qrCode);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_CHECK_IN_SUCCESS);
    }

    @Operation(summary = "퇴근용 QR 코드 생성", description = "퇴근을 위한 QR 코드를 생성합니다.")
    @PostMapping("/check-out/qr/generate")
    public ResponseEntity<String> generateCheckOutQRCode() {
        String qrCodeBase64 = attendanceCommandService.generateQRCodeForCheckOut();
        return ResponseEntity.ok(qrCodeBase64);
    }

    @Operation(summary = "퇴근 처리", description = "QR 코드 검증 후 퇴근을 처리합니다.")
    @PostMapping("/check-out")
    public ResponseEntity<SuccessCode> validateAndCheckOut(@RequestParam String qrCode) {
        attendanceCommandService.validateAndCheckOut(qrCode);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_CHECK_OUT_SUCCESS);
    }
}
