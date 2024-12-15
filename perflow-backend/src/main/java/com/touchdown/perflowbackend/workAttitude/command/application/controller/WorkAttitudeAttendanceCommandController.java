package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAttendanceRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeAttendanceCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WorkAttribute-Attendance-Controller", description = "출퇴근 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/emp/attendances")
public class WorkAttitudeAttendanceCommandController {

    private final WorkAttitudeAttendanceCommandService workAttitudeAttendanceCommandService;

    @Operation(summary = "출근 등록", description = "출근을 등록")
    @PostMapping("/check-in")
    public ResponseEntity<SuccessCode> checkIn(@RequestBody WorkAttitudeAttendanceRequestDTO workAttitudeAttendanceRequestDTO) {
        workAttitudeAttendanceCommandService.checkIn(workAttitudeAttendanceRequestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_CHECK_IN_SUCCESS);
    }

    @Operation(summary = "퇴근 등록", description = "퇴근을 등록")
    @PostMapping("/check-out")
    public ResponseEntity<SuccessCode> checkOut(@RequestBody WorkAttitudeAttendanceRequestDTO workAttitudeAttendanceRequestDTO) {
        workAttitudeAttendanceCommandService.checkOut(workAttitudeAttendanceRequestDTO);
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_CHECK_OUT_SUCCESS);
    }
}
