package com.touchdown.perflowbackend.workAttitude.command.application.service;

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
    private final WorkAttitudeAnnualCommandRepository annualRepository;


    // 현재 로그인한 사용자 조회
    private Employee getCurrentEmployee() {
        String empId = EmployeeUtil.getEmpId();
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }


    // 휴가 신청
    @Transactional
    public void registerVacation(WorkAttitudeVacationRequestDTO requestDTO) {

        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApprover())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

// 날짜 중복 검증
        validateDateOverlap(employee.getEmpId(), requestDTO.getVacationStart(), requestDTO.getVacationEnd());

// 매퍼 호출 및 저장
        Vacation vacation = WorkAttitudeVacationMapper.toEntity(requestDTO, employee, approver);
        vacationRepository.save(vacation);
        log.info("휴가 신청 완료: {}", vacation);
    }

    // 휴가 수정
    @Transactional
    public void updateVacation(Long vacationId, WorkAttitudeVacationRequestDTO requestDTO) {


        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VACATION));
        Employee employee = getCurrentEmployee();
        if (!vacation.getEmpId().getEmpId().equals(employee.getEmpId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }
        if (vacation.getVacationStatus() == VacationStatus.CONFIRMED) {
            throw new CustomException(ErrorCode.ALREADY_CONFIRMED);
        }

        // 수정 요청 날짜 중복 검증 추가 (휴가 + 연차 일정 검증)
        validateDateOverlap(employee.getEmpId(), requestDTO.getVacationStart(), requestDTO.getVacationEnd());

        WorkAttitudeVacationMapper.updateEntityFromDto(requestDTO, vacation);

        Employee approver = employeeRepository.findById(requestDTO.getApprover())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
        vacation.updateApprover(approver); // 결재자 재지정
        vacation.updateVacationStatus(VacationStatus.PENDING, null);


        vacationRepository.save(vacation);
        log.info("휴가 수정 완료: {}", vacation);
    }

    // 휴가 삭제 (소프트 삭제)
    @Transactional
    public void softDeleteVacation(Long vacationId) {
        Employee employee = getCurrentEmployee();
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VACATION));

        if (!vacation.getEmpId().getEmpId().equals(employee.getEmpId())) {
            throw new CustomException(ErrorCode.NOT_MATCH_WRITER);
        }

        vacation.softDelete();
        vacationRepository.save(vacation);
        log.info("휴가 삭제 완료: {}", vacation);
    }

    // 휴가 승인
    @Transactional
    public void approveVacation(Long vacationId) {
        Employee approver = getCurrentEmployee();
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VACATION));

        if (!vacation.getApprover().getEmpId().equals(approver.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 예외 발생
        }

        vacation.approveVacation();
        vacation.setUpdateDatetime(LocalDateTime.now());
        vacationRepository.save(vacation);
        log.info("휴가 승인 완료: {}", vacation);
    }

    // 휴가 반려
    @Transactional
    public void rejectVacation(Long vacationId, String rejectReason) {
        Employee approver = getCurrentEmployee();
        Vacation vacation = vacationRepository.findById(vacationId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_VACATION));

        if (!vacation.getApprover().getEmpId().equals(approver.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED); // 권한 없음 예외 발생
        }

        vacation.rejectVacation(rejectReason);
        vacation.setUpdateDatetime(LocalDateTime.now());
        vacationRepository.save(vacation);
        log.info("휴가 반려 완료: {}", vacation);
    }

    // 날짜 중복 검증
    private void validateDateOverlap(String empId, LocalDateTime startDate, LocalDateTime endDate) {
        boolean overlap = vacationRepository.existsByEmpIdAndStatusAndVacationStartAndVacationEnd(
                empId, Status.ACTIVATED, endDate, startDate);


        // 연차 중복 체크 추가
        boolean annualOverlap = annualRepository.existsByEmpId_EmpIdAndStatusAndAnnualStartLessThanEqualAndAnnualEndGreaterThanEqual(
                empId, Status.ACTIVATED, endDate, startDate);
        if (overlap || annualOverlap) {

            throw new CustomException(ErrorCode.DUPLICATE_VACATION);
        }
    }
}
