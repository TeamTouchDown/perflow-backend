package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAttendanceRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAttendanceCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeAttendanceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAttendanceCommandService {

    private final WorkAttitudeAttendanceCommandRepository workAttitudeAttendanceCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;

    @Transactional
    public void createAttendance(WorkAttitudeAttendanceRequestDTO workAttitudeAttendanceRequestDTO) {
        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);

        Attendance attendance = WorkAttitudeAttendanceMapper.toEntity(workAttitudeAttendanceRequestDTO, employee);
        workAttitudeAttendanceCommandRepository.save(attendance);
    }

    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }
}
