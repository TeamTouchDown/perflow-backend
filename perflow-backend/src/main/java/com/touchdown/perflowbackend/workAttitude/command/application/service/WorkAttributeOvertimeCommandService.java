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
import org.hibernate.sql.ast.tree.expression.Over;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttributeOvertimeCommandService {

    private final WorkAttributeOvertimeCommandRepository workAttributeOvertimeCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository; // string 값
    private final ApproveCommandRepository approveCommandRepository;

    @Transactional
    public void createOvertime(WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);
        ApproveSbj approveSbj = findApproveSbjById(workAttributeOvertimeForEmployeeRequestDTO.getApproveSbjId());

        Overtime overtime = WorkAttributeOvertimeMapper.toEntity(workAttributeOvertimeForEmployeeRequestDTO, employee, approveSbj);
        workAttributeOvertimeCommandRepository.save(overtime);

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
    public void updateOvertime(Long overtimeId, WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {

        Overtime overtime = findOvertimeById(overtimeId);
        overtime.updateOvertimeStatus(
                workAttributeOvertimeForEmployeeRequestDTO.getOvertimeStatus(),
                workAttributeOvertimeForEmployeeRequestDTO.getOvertimeRetroactiveReason() // 반려 사유
        );
        workAttributeOvertimeCommandRepository.save(overtime);

    }

    private Overtime findOvertimeById(Long overtimeId) {
        return workAttributeOvertimeCommandRepository.findById(overtimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPROVE_SBJ));
    }

    @Transactional
    public void deleteOvertime(Long overtimeId) {
        Overtime overtime = findOvertimeById(overtimeId);
        overtime.deleteOvertime();
        workAttributeOvertimeCommandRepository.save(overtime);
    }

    @Transactional
    public void updateOvertimeStatus(Long overtimeId, WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
        Overtime overtime = findOvertimeById(overtimeId);

        if ("REJECTED".equals(workAttributeOvertimeForEmployeeRequestDTO.getOvertimeStatus())) {
            overtime.updateOvertimeStatus(Status.REJECTED, workAttributeOvertimeForEmployeeRequestDTO.getOvertimeRetroactiveReason());
        } else if ("APPROVED".equals(workAttributeOvertimeForEmployeeRequestDTO.getOvertimeStatus())) {
            overtime.updateOvertimeStatus(Status.CONFIRMED, null); // 승인 시 반려 사유 초기화
        } else {
            throw new CustomException(ErrorCode.INVALID_OVERTIME_STATUS); // 유효하지 않은 상태 에러 처리
        }

        workAttributeOvertimeCommandRepository.save(overtime);
    }


}
