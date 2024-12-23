package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveSbjCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeVacationRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAnnualCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeVacationCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeVacationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeVacationCommandService {

    private final WorkAttitudeVacationCommandRepository vacationRepository;
    private final EmployeeCommandRepository employeeRepository;
    private final ApproveSbjCommandRepository approveSbjRepository;
    private final WorkAttitudeAnnualCommandRepository workAttitudeAnnualCommandRepository;

    // 휴가 신청
    @Transactional
    public void registerVacation(WorkAttitudeVacationRequestDTO requestDTO) {
        log.info("휴가 신청 등록: {}", requestDTO);

        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);

        // 중복 검증
        if (isOverlappingLeave(empId, requestDTO.getVacationStart(), requestDTO.getVacationEnd())) {
            throw new CustomException(ErrorCode.DUPLICATE_VACATION);
        }

        ApproveSbj approveSbj = approveSbjRepository.findById(requestDTO.getApproveSbjId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));

        Vacation vacation = WorkAttitudeVacationMapper.toEntity(requestDTO, employee, approveSbj);
        vacationRepository.save(vacation);
    }


    // 휴가 수정
    @Transactional
    public void updateVacation(Long vacationId, WorkAttitudeVacationRequestDTO requestDTO) {
        log.info("휴가 수정: {}, {}", vacationId, requestDTO);

        Vacation vacation = findVacationById(vacationId);

        // 중복 검증
        if (isOverlappingLeave(vacation.getEmpId().getEmpId(), requestDTO.getVacationStart(), requestDTO.getVacationEnd())) {
            throw new CustomException(ErrorCode.DUPLICATE_VACATION);
        }

        WorkAttitudeVacationMapper.updateEntityFromDto(requestDTO, vacation);
        vacationRepository.save(vacation);
    }


    // 휴가 삭제 (소프트 삭제)
    @Transactional
    public void deleteVacation(Long vacationId) {
        log.info("휴가 삭제: {}", vacationId);

        Vacation vacation = findVacationById(vacationId);
        vacation.softDelete();
        vacationRepository.save(vacation);
    }

    // 휴가 승인
    @Transactional
    public void approveVacation(Long vacationId) {
        log.info("휴가 승인: {}", vacationId);

        Vacation vacation = findVacationById(vacationId);
        vacation.approveVacation();
        vacationRepository.save(vacation);
    }

    // 휴가 반려
    @Transactional
    public void rejectVacation(Long vacationId, String rejectReason) {
        log.info("휴가 반려: {}, 사유: {}", vacationId, rejectReason);

        Vacation vacation = findVacationById(vacationId);
        vacation.rejectVacation(rejectReason);
        vacationRepository.save(vacation);
    }



    // 사원 조회
    private Employee findEmployeeByEmpId(String empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    // 휴가 조회
    private Vacation findVacationById(Long vacationId) {
        return vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VACATION));
    }

    private boolean isOverlappingLeave(String empId, LocalDateTime start, LocalDateTime end) {
        // 휴가 중복 체크
        boolean vacationOverlap = vacationRepository.existsByEmpIdAndVacationStartBeforeAndVacationEndAfter(
                empId, end, start);

        // 연차 중복 체크
        boolean annualOverlap = workAttitudeAnnualCommandRepository.existsByEmpIdAndAnnualStartBeforeAndAnnualEndAfter(
                empId, end, start);

        return vacationOverlap || annualOverlap; // 하나라도 겹치면 true 반환
    }

}
