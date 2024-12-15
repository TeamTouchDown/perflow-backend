package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAttendanceRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AttendanceStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAttendanceCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttitudeAttendanceCommandService {

    private final WorkAttitudeAttendanceCommandRepository workAttitudeAttendanceCommandRepository;
    private final EmployeeCommandRepository employeeCommandRepository;



    @Transactional
    public void checkIn(WorkAttitudeAttendanceRequestDTO workAttitudeAttendanceRequestDTO) {

        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);
        if (workAttitudeAttendanceCommandRepository.existsByEmpIdAndCheckOutDateTimeIsNull(employee)){
            throw new CustomException(ErrorCode.ALREADY_CHECKED_IN);
        }
        Attendance attendance = Attendance.builder()
                .empId(employee)
                .checkInDateTime(workAttitudeAttendanceRequestDTO.getCheckInDateTime())
                .status(AttendanceStatus.WORK)
                .build();

    }
    @Transactional
    public void checkOut(WorkAttitudeAttendanceRequestDTO workAttitudeAttendanceRequestDTO) {

        String empId = EmployeeUtil.getEmpId();
        Employee employee = findEmployeeByEmpId(empId);

        Attendance attendance = workAttitudeAttendanceCommandRepository.findByEmpIdAndCheckOutDateTimeIsNull(employee)
                .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_ATTENDANCE));
        attendance.updateCheckOut(workAttitudeAttendanceRequestDTO.getCheckOutDateTime(), AttendanceStatus.OFF);
        workAttitudeAttendanceCommandRepository.save(attendance);



    }

    private Employee findEmployeeByEmpId(String empId) {
        return employeeCommandRepository.findById(empId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMPLOYEE));
    }
}
