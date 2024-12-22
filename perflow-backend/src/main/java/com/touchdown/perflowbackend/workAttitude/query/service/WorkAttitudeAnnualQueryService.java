package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAttendanceQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAnnualQueryService {

    private final WorkAttitudeAttendanceQueryRepository attendanceRepository;
    private final EmployeeQueryRepository employeeRepository;
}
