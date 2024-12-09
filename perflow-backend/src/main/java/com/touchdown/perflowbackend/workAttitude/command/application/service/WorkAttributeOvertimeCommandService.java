package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveCommandRepository;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttributeOvertimeForEmployeeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttributeOvertimeCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkAttributeOvertimeCommandService {

    private final WorkAttributeOvertimeCommandRepository workAttributeOvertimeCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository; // string 값
    private final ApproveCommandRepository approveCommandRepository;

    public void createOvertime(WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
    }

    public void updateOvertime(Long overtimeId, WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
    }

    public void deleteOvertime(Long overtimeId) {
    }

    public void updateOvertimeStatus(Long overtimeId, WorkAttributeOvertimeForEmployeeRequestDTO workAttributeOvertimeForEmployeeRequestDTO) {
    }
}
