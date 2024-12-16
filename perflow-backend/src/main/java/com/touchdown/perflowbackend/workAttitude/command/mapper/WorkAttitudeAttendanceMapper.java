package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAttendanceRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceSummaryResponseDTO;

public class WorkAttitudeAttendanceMapper {

    public static Attendance toEntity(WorkAttitudeAttendanceRequestDTO requestDTO, Employee employee){
        return Attendance.builder()
                .empId(employee)
                .checkInDateTime(requestDTO.getCheckInDateTime())
                .checkOutDateTime(requestDTO.getCheckOutDateTime())
                .attendanceStatus(requestDTO.getAttendanceStatus())
                .build();
    }

    public static WorkAttitudeAttendanceResponseDTO toResponseDTO(Attendance attendance) {
        return WorkAttitudeAttendanceResponseDTO.builder()
                .attendanceId(attendance.getAttendanceId())
                .empId(attendance.getEmpId().getEmpId())
                .checkInDateTime(attendance.getCheckInDateTime())
                .checkOutDateTime(attendance.getCheckOutDateTime())
                .attendanceStatus(attendance.getAttendanceStatus())
                .build();
    }

    public static WorkAttitudeAttendanceSummaryResponseDTO workAttitudeAttendanceSummaryResponseDTO(String period, long totalMinutes) {
        int hours = (int) (totalMinutes / 60);
        int minutes = (int) (totalMinutes % 60);
        return WorkAttitudeAttendanceSummaryResponseDTO.builder()
                .period(period)
                .totalHours(hours)
                .totalMinutes(minutes)
                .build();
    }
}
