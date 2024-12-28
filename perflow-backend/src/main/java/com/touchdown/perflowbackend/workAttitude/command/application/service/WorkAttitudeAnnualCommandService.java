package com.touchdown.perflowbackend.workAttitude.command.application.service;

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
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeAnnualCommandService {

    private final WorkAttitudeAnnualCommandRepository annualRepository;
    private final EmployeeCommandRepository employeeRepository;

    // 현재 로그인한 사용자 조회
    private Employee getCurrentEmployee() {
        String empId = EmployeeUtil.getEmpId();
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    // 연차 신청
    @Transactional
    public void registerAnnual(WorkAttitudeAnnualRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApprover())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

        validateDateOverlap(employee.getEmpId(), requestDTO.getAnnualStart(), requestDTO.getAnnualEnd());
        validateAnnualCount(employee, requestDTO.getAnnualStart(), requestDTO.getAnnualEnd(), requestDTO.getAnnualType());

        Annual annual = WorkAttitudeAnnualMapper.toEntity(requestDTO, employee, approver);
        annualRepository.save(annual);
        log.info("연차 신청 완료: {}", annual);
    }

    // 연차 수정
    @Transactional
    public void updateAnnual(Long annualId, WorkAttitudeAnnualRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Annual annual = annualRepository.findById(annualId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANNUAL));

        if (!annual.getEmpId().getEmpId().equals(employee.getEmpId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        WorkAttitudeAnnualMapper.updateEntityFromDto(requestDTO, annual);
        annualRepository.save(annual);
        log.info("연차 수정 완료: {}", annual);
    }

    // 연차 삭제 (소프트 삭제)
    @Transactional
    public void softDeleteAnnual(Long annualId) {
        Employee employee = getCurrentEmployee();
        Annual annual = annualRepository.findById(annualId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANNUAL));

        if (!annual.getEmpId().getEmpId().equals(employee.getEmpId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        annual.softDelete();
        annualRepository.save(annual);
        log.info("연차 삭제 완료: {}", annual);
    }

    // 연차 승인
    @Transactional
    public void approveAnnual(Long annualId) {
        Employee approver = getCurrentEmployee();
        Annual annual = annualRepository.findById(annualId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANNUAL));

        if (!annual.getApprover().getEmpId().equals(approver.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 예외 발생
        }
        annual.setAnnualStatus(Status.CONFIRMED);
        annual.setUpdateDatetime(LocalDateTime.now());
        annualRepository.save(annual);
        log.info("연차 승인 완료: {}", annual);
    }

    // 연차 반려
    @Transactional
    public void rejectAnnual(Long annualId, String rejectReason) {
        Employee approver = getCurrentEmployee();
        Annual annual = annualRepository.findById(annualId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANNUAL));

        if (!annual.getApprover().getEmpId().equals(approver.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 예외 발생
        }

        annual.setAnnualStatus(Status.REJECTED);
        annual.setAnnualRejectReason(rejectReason);
        annual.setUpdateDatetime(LocalDateTime.now());
        annualRepository.save(annual);
        log.info("연차 반려 완료: {}", annual);
    }

    // 날짜 중복 검증
    private void validateDateOverlap(String empId, LocalDateTime startDate, LocalDateTime endDate) {
        boolean overlap = annualRepository.existsByEmpId_EmpIdAndStatusAndAnnualStartLessThanEqualAndAnnualEndGreaterThanEqual(
                empId, Status.ACTIVATED, endDate, startDate);
        if (overlap) {
            throw new CustomException(ErrorCode.DUPLICATE_ANNUAL);
        }
    }

    // 연차 개수 검증
    private void validateAnnualCount(Employee employee, LocalDateTime start, LocalDateTime endDate, AnnualType annualType) {
        int providedAnnualCount = calculateProvidedAnnualCount(employee.getJoinDate());
        long usedAnnualCount = annualRepository.countByEmpId_EmpIdAndStatus(employee.getEmpId(), Status.CONFIRMED);
        double requestedDays = calculateDaysBetween(start, endDate, annualType);

        if ((usedAnnualCount + requestedDays) > providedAnnualCount) {
            throw new CustomException(ErrorCode.NOT_ENOUGH_ANNUAL);
        }
    }

    private int calculateProvidedAnnualCount(LocalDate joinDate) {
        int baseAnnual = 15;
        int maxAnnual = 25;
        int years = LocalDate.now().getYear() - joinDate.getYear();
        int additionalAnnual = (years / 3);
        return Math.min(baseAnnual + additionalAnnual, maxAnnual);
    }

    private double calculateDaysBetween(LocalDateTime startDate, LocalDateTime endDate, AnnualType type) {
        long days = java.time.Duration.between(startDate.toLocalDate().atStartOfDay(), endDate.toLocalDate().atStartOfDay()).toDays() + 1;
        if (type == AnnualType.MORNINGHALF || type == AnnualType.AFTERNOONHALF) {
            return days - 0.5;
        }
        return days;
    }
}
