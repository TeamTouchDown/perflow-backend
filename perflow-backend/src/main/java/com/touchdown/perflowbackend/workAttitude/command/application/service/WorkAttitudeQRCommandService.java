package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.util.QRCodeGenerator;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkAttitudeQRCommandService {

    private final QRCodeGenerator qrCodeGenerator;

    // QR 코드 생성 (로그인한 사용자만 접근 가능)
    public String generateQRCode() {
        String empId = EmployeeUtil.getEmpId(); // 로그인한 사용자 empId 가져오기
        return qrCodeGenerator.generateQRCode(empId);
    }

    // QR 코드 검증 (로그인한 사용자만 검증 가능)
    public void validateQRCode(String code) {
        String empId = EmployeeUtil.getEmpId(); // 로그인한 사용자 empId 가져오기
        boolean isValid = qrCodeGenerator.validateQRCode(empId, code);

        if (!isValid) {
            throw new CustomException(ErrorCode.INVALID_QR_CODE);
        }
    }
}
