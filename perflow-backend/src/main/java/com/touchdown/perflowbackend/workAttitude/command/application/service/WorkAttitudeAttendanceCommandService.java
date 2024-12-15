package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.util.QRCodeGenerator;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AttendanceStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAttendanceCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAttendanceCommandService {

    private final WorkAttitudeAttendanceCommandRepository attendanceRepository;
    private final EmployeeCommandRepository employeeRepository;
    private final QRCodeGenerator qrCodeGenerator;

    @Transactional
    public void checkInWithQR() {
        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);

        // 출근 여부 확인
        if (attendanceRepository.existsByEmpIdAndCheckOutDateTimeIsNull(employee)) {
            throw new CustomException(ErrorCode.ALREADY_CHECKED_IN);
        }

        // QR 생성 및 인증
        String qrCode = qrCodeGenerator.generateQRCode(empId);
        boolean isValidated = qrCodeGenerator.validateQRCode(empId, qrCode);

        if (!isValidated) {
            throw new CustomException(ErrorCode.INVALID_QR_CODE);
        }

        // 출근 처리
        Attendance attendance = Attendance.builder()
                .empId(employee)
                .checkInDateTime(LocalDateTime.now())
                .attendanceStatus(AttendanceStatus.WORK)
                .build();

        attendanceRepository.save(attendance);
    }

    @Transactional
    public void checkOutWithQR() {
        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);

        // 퇴근 여부 확인
        Attendance attendance = attendanceRepository.findByEmpIdAndCheckOutDateTimeIsNull(employee)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ATTENDANCE));

        // QR 생성 및 인증
        String qrCode = qrCodeGenerator.generateQRCode(empId);
        boolean isValidated = qrCodeGenerator.validateQRCode(empId, qrCode);

        if (!isValidated) {
            throw new CustomException(ErrorCode.INVALID_QR_CODE);
        }

        // 퇴근 처리
        attendance.updateCheckOut(LocalDateTime.now(), AttendanceStatus.OFF);
        attendanceRepository.save(attendance);
    }

    private Employee findEmployeeByEmpId(String empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }
}
