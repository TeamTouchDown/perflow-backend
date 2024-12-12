package com.touchdown.perflowbackend.workAttitude.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAttendanceRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.service.WorkAttitudeAttendanceCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "WorkAttribute-Attendance-Controller", description = "출퇴근 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeAttendanceCommandController {
    private final WorkAttitudeAttendanceCommandService workAttitudeAttendanceCommandService;
    @Operation(summary = "출퇴근 등록",description = "출퇴근을 등록합니다")
    @PostMapping("/emp/attendances")
    public ResponseEntity<SuccessCode>createAttendance(@RequestBody WorkAttitudeAttendanceRequestDTO workAttitudeAttendanceRequestDTO){
        workAttitudeAttendanceCommandService.createAttendance(workAttitudeAttendanceRequestDTO);

        // 성공 코드를 반환
        return ResponseEntity.ok(SuccessCode.WORK_ATTITUDE_ATTENDANCE_SUCCESS);
    }

}
