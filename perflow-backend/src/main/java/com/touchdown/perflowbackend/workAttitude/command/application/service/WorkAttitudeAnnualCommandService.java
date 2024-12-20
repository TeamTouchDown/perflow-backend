package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAnnualCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttitudeAnnualCommandService {

    private final WorkAttitudeAnnualCommandRepository workAttitudeAnnualCommandRepository;
    private final EmployeeCommandRepository employeeRepository;

    @Transactional


}
