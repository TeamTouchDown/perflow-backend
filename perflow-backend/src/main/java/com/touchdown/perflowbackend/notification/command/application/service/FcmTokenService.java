package com.touchdown.perflowbackend.notification.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.notification.command.application.dto.FcmTokenRequestDTO;
import com.touchdown.perflowbackend.notification.command.domain.aggregate.FcmToken;
import com.touchdown.perflowbackend.notification.command.domain.repository.FcmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FcmTokenService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void registerFcmToken(FcmTokenRequestDTO fcmTokenRequestDTO) {

        Employee employee = employeeCommandRepository.findById(fcmTokenRequestDTO.getEmpId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));

        if (fcmTokenRepository.findByFcmToken(fcmTokenRequestDTO.getToken()).isPresent()) {
            return;
        }

        FcmToken fcmToken = FcmToken.builder()
                .employee(employee)
                .fcmToken(fcmTokenRequestDTO.getToken())
                .deviceType(fcmTokenRequestDTO.getDeviceType())
                .build();

        fcmTokenRepository.save(fcmToken);
    }

    @Transactional
    public void deleteFcmToken(String token) {
        fcmTokenRepository.deleteByFcmToken(token);
    }
}
