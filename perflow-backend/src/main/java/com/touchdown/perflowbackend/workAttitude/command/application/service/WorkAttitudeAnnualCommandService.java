package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveSbjCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAnnualRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAnnualCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeAnnualMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeAnnualCommandService {

    private final WorkAttitudeAnnualCommandRepository annualRepository;
    private final EmployeeCommandRepository employeeRepository;
    private final ApproveSbjCommandRepository approveSbjRepository;

    // 연차 신청
    @Transactional
    public void registerAnnual(WorkAttitudeAnnualRequestDTO requestDTO) {
        log.info("연차 신청 등록: {}", requestDTO);

        String empId = EmployeeUtil.getEmpId(); // 로그인 사용자 ID 가져오기
        Employee employee = findEmployeeByEmpId(empId);

        // 소급 여부 검증
        boolean isRetroactive = requestDTO.getIsAnnualRetroactive();
        LocalDateTime now = LocalDateTime.now();
        if (requestDTO.getAnnualStart().isBefore(now) && !isRetroactive) {
            throw new CustomException(ErrorCode.INVALID_RETROACTIVE_DECISION);
        }

        // 기존 결재 주체 조회
        ApproveSbj approveSbj = approveSbjRepository.findById(requestDTO.getApproveSbjId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));

        Annual annual = WorkAttitudeAnnualMapper.toEntity(requestDTO, employee, approveSbj);
        annualRepository.save(annual);
    }

    // 연차 수정
    @Transactional
    public void updateAnnual(Long annualId, WorkAttitudeAnnualRequestDTO requestDTO) {
        log.info("연차 수정: {}, {}", annualId, requestDTO);

        Annual annual = findAnnualById(annualId);

        WorkAttitudeAnnualMapper.updateEntityFromDto(requestDTO, annual);
        annual.setStatus(Status.UPDATED);

        annualRepository.save(annual);
    }

    // 연차 삭제 (소프트 삭제)
    @Transactional
    public void softDeleteAnnual(Long annualId) {
        log.info("연차 삭제: {}", annualId);

        Annual annual = findAnnualById(annualId);
        annual.softDelete();
        annualRepository.save(annual);
    }

    // 연차 승인
    @Transactional
    public void approveAnnual(Long annualId) {
        log.info("연차 승인: {}", annualId);

        Annual annual = findAnnualById(annualId);
        annual.approveAnnual();
        annualRepository.save(annual);
    }

    // 연차 반려
    @Transactional
    public void rejectAnnual(Long annualId, String rejectReason) {
        log.info("연차 반려: {}, 사유: {}", annualId, rejectReason);

        Annual annual = findAnnualById(annualId);
        annual.rejectAnnual(rejectReason);
        annualRepository.save(annual);
    }

    // 사원 조회
    private Employee findEmployeeByEmpId(String empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    // 연차 조회
    private Annual findAnnualById(Long annualId) {
        return annualRepository.findById(annualId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));
    }
}
