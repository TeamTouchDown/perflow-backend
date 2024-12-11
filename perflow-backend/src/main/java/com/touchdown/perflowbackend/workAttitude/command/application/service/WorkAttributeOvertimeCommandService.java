package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveCommandRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttributeOvertimeForEmployeeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttributeOvertimeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttributeOvertimeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttributeOvertimeCommandService {

    private final WorkAttributeOvertimeCommandRepository workAttributeOvertimeCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;
    private final ApproveCommandRepository approveCommandRepository;

    // 신규 초과근무 생성
    @Transactional
    public void createOvertime(WorkAttributeOvertimeForEmployeeRequestDTO requestDTO) {
        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);
        ApproveSbj approveSbj = findApproveSbjById(requestDTO.getApproveSbjId());

        Overtime overtime = WorkAttributeOvertimeMapper.toEntity(requestDTO, employee, approveSbj);
        workAttributeOvertimeCommandRepository.save(overtime);
    }

    // 소급 신청
    @Transactional
    public void applyForRetroactiveOvertime(Long overtimeId, String reason) {
        Overtime overtime = findOvertimeById(overtimeId);

        if (overtime.getIsOvertimeRetroactive()) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED_RETROACTIVE);
        }

        overtime.updateRetroactiveStatus(Status.PENDING, reason);
        overtime.updateOvertimeRetroactive(true);
        workAttributeOvertimeCommandRepository.save(overtime);
    }

    // 소급 승인/반려
    @Transactional
    public void approveOrRejectRetroactiveOvertime(Long overtimeId, String decision, String reason) {
        Overtime overtime = findOvertimeById(overtimeId);

        if ("APPROVED".equalsIgnoreCase(decision)) {
            overtime.updateRetroactiveStatus(Status.CONFIRMED, null); // 승인 처리
        } else if ("REJECTED".equalsIgnoreCase(decision)) {
            overtime.updateRetroactiveStatus(Status.REJECTED, reason); // 반려 처리
        } else {
            throw new CustomException(ErrorCode.INVALID_RETROACTIVE_DECISION);
        }

        workAttributeOvertimeCommandRepository.save(overtime);
    }

    // 초과근무 삭제
    @Transactional
    public void deleteOvertime(Long overtimeId) {
        Overtime overtime = findOvertimeById(overtimeId);
        overtime.deleteOvertime();
        workAttributeOvertimeCommandRepository.save(overtime);
    }

    // Helper methods
    private Overtime findOvertimeById(Long overtimeId) {
        return workAttributeOvertimeCommandRepository.findById(overtimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_OVERTIME));
    }

    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }

    private ApproveSbj findApproveSbjById(Long approveSbjId) {
        return approveCommandRepository.findById(approveSbjId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));
    }
    @Transactional
    public void updateOvertimeStatus(Long overtimeId, WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        // 1. 초과근무 데이터 조회
        Overtime overtime = findOvertimeById(overtimeId);

        // 2. 상태에 따른 로직 처리
        if (workAttributeOvertimeForEmployeeRequestDTO.getOvertimeStatus() == Status.REJECTED) {
            // 상태가 반려인 경우 반려 사유 설정
            overtime.updateOvertimeStatus(Status.REJECTED, workAttributeOvertimeForEmployeeRequestDTO.getOvertimeRetroactiveReason());
        } else if (workAttributeOvertimeForEmployeeRequestDTO.getOvertimeStatus() == Status.CONFIRMED) {
            // 상태가 승인인 경우 반려 사유 초기화
            overtime.updateOvertimeStatus(Status.CONFIRMED, null);
        } else {
            // 유효하지 않은 상태인 경우 예외 발생
            throw new CustomException(ErrorCode.INVALID_OVERTIME_STATUS);
        }

        // 3. 업데이트된 엔티티 저장
        workAttributeOvertimeCommandRepository.save(overtime);
    }

}
