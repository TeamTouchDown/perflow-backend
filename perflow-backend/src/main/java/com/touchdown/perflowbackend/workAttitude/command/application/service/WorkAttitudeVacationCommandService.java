package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveSbjCommandRepository;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeVacationCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeVacationCommandService {

    private final WorkAttitudeVacationCommandRepository workAttitudeVacationCommandRepository;
    private final EmployeeCommandRepository employeeRepository;
    private final ApproveSbjCommandRepository approveSbjRepository;

}
