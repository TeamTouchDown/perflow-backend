package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeOvertimeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeOvertimeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeOvertimeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeOvertimeCommandService {

    private final WorkAttitudeOvertimeCommandRepository overtimeRepository;
    private final EmployeeCommandRepository employeeRepository;

    // 현재 로그인한 사원 정보 가져오기
    private Employee getCurrentEmployee() {
        String empId = EmployeeUtil.getEmpId();
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }
    

    // 중복 검증 메서드 추가
    private boolean isOvertimeOverlap(String empId, LocalDateTime start, LocalDateTime end) {
        return overtimeRepository.existsByEmpIdAndStatusAndOvertimeStartLessThanEqualAndOvertimeEndGreaterThanEqual(
                empId, Status.ACTIVATED, end, start);
    }


    // 초과근무 신청
    @Transactional
    public void createOvertime(WorkAttitudeOvertimeRequestDTO requestDTO) {
        Employee employee = getCurrentEmployee();
        Employee approver = employeeRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));

        // 중복 검증 추가
        if (isOvertimeOverlap(employee.getEmpId(), requestDTO.getOvertimeStart(), requestDTO.getOvertimeEnd())) {
            throw new CustomException(ErrorCode.DUPLICATE_OVERTIME);
        }

        // 야간 근무 조건 검증
        if (requestDTO.getOvertimeType() == OvertimeType.NIGHT) {
            validateNightOvertime(requestDTO.getOvertimeStart(), requestDTO.getOvertimeEnd());
        }

        Overtime overtime = WorkAttitudeOvertimeMapper.toEntity(requestDTO, employee, approver);
        overtimeRepository.save(overtime);
        log.info("초과근무 신청 완료: {}", overtime);
    }

    private void validateNightOvertime(LocalDateTime start, LocalDateTime end) {
        LocalTime startLimit = LocalTime.of(18, 0);
        LocalTime endLimit = LocalTime.of(9, 0);

        if (start.toLocalTime().isBefore(startLimit) && start.toLocalTime().isAfter(endLimit)) {
            throw new CustomException(ErrorCode.INVALID_OVERTIME_REQUEST);
        }

        if (end.toLocalTime().isAfter(endLimit) && end.toLocalTime().isBefore(startLimit)) {
            throw new CustomException(ErrorCode.INVALID_OVERTIME_REQUEST);
        }
    }

    // 초과근무 수정
    @Transactional
    public void updateOvertime(Long overtimeId, WorkAttitudeOvertimeRequestDTO requestDTO) {

        Overtime overtime = overtimeRepository.findById(overtimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OVERTIME));

        Employee employee = getCurrentEmployee();

        if (!overtime.getEmpId().getEmpId().equals(EmployeeUtil.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        if (overtime.getOvertimeStatus() == Status.CONFIRMED) {
            throw new CustomException(ErrorCode.ALREADY_CONFIRMED);
        }

        Employee approver = employeeRepository.findById(requestDTO.getApproverId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
        overtime.updateApprover(approver); // 결재자 재지정
        overtime.resetStatusToPending();  // 상태 초기화


        overtime.updateOvertime(
                requestDTO.getOvertimeType() != null ? requestDTO.getOvertimeType() : overtime.getOvertimeType(),
                requestDTO.getOvertimeStart() != null ? requestDTO.getOvertimeStart() : overtime.getOvertimeStart(),
                requestDTO.getOvertimeEnd() != null ? requestDTO.getOvertimeEnd() : overtime.getOvertimeEnd(),
                requestDTO.getOvertimeRetroactiveReason() != null ? requestDTO.getOvertimeRetroactiveReason() : overtime.getOvertimeRetroactiveReason(),
                requestDTO.getIsOvertimeRetroactive() != null ? requestDTO.getIsOvertimeRetroactive() : overtime.getIsOvertimeRetroactive()
        );

        overtimeRepository.save(overtime);
        log.info("초과근무 수정 완료: {}", overtime);
    }

    // 초과근무 삭제 (소프트 삭제)
    @Transactional
    public void deleteOvertime(Long overtimeId) {
        Overtime overtime = overtimeRepository.findById(overtimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OVERTIME));

        if (!overtime.getEmpId().getEmpId().equals(EmployeeUtil.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        overtime.deleteOvertime();
        overtimeRepository.save(overtime);
        log.info("초과근무 삭제 완료: {}", overtime);
    }

    // 초과근무 승인
    @Transactional
    public void approveOvertime(Long overtimeId) {
        Employee approver = getCurrentEmployee();
        Overtime overtime = overtimeRepository.findById(overtimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OVERTIME));

        if (!overtime.getApprover().getEmpId().equals(approver.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        overtime.updateOvertimeStatus(Status.CONFIRMED, null);
        overtimeRepository.save(overtime);
        log.info("초과근무 승인 완료: {}", overtime);
    }

    // 초과근무 반려
    @Transactional
    public void rejectOvertime(Long overtimeId, String rejectReason) {
        Employee approver = getCurrentEmployee();
        Overtime overtime = overtimeRepository.findById(overtimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OVERTIME));

        if (!overtime.getApprover().getEmpId().equals(approver.getEmpId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        overtime.updateOvertimeStatus(Status.REJECTED, rejectReason);
        overtimeRepository.save(overtime);
        log.info("초과근무 반려 완료: {}", overtime);
    }
}
