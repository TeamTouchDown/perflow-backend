package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAttendanceRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceResponseDTO;

public class WorkAttitudeAttendanceMapper {

    public static Attendance toEntity(WorkAttitudeAttendanceRequestDTO requestDTO, Employee employee){
        return Attendance.builder()
                .empId(employee)
                .checkInDateTime(requestDTO.getCheckInDateTime())
                .checkOutDateTime(requestDTO.getCheckOutDateTime())
                .status(requestDTO.getAttendanceStatus())
                .build();
    }

    public static WorkAttitudeAttendanceResponseDTO toResponseDTO(Attendance attendance) {
        return WorkAttitudeAttendanceResponseDTO.builder()
                .attendanceId(attendance.getAttendanceId())
                .empId(attendance.getEmpId().getEmpId())
                .checkInDateTime(attendance.getCheckInDateTime())
                .checkOutDateTime(attendance.getCheckOutDateTime())
                .AttendanceStatus(attendance.getAttendanceStatus())
                .build();
    }
}
