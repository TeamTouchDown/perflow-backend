package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeOvertimeForEmployeeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForEmployeeSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForTeamLeaderSummaryDTO;

import java.time.Duration;
import java.util.List;

public class WorkAttitudeOvertimeMapper {

    public static Overtime toEntity(WorkAttitudeOvertimeForEmployeeRequestDTO requestDTO, Employee employee, ApproveSbj approveSbj) {
        return Overtime.builder()
                .empId(employee) // 사원 정보
                .approveSbjId(approveSbj) // 승인자 정보
                .overtimeType(requestDTO.getOvertimeType()) // 초과 근무 유형
                .enrollOvertime(requestDTO.getEnrollOvertime()) // 신청 일자
                .overtimeStart(requestDTO.getOvertimeStart()) // 초과 근무 시작 시간
                .overtimeEnd(requestDTO.getOvertimeEnd()) // 초과 근무 종료 시간
                .overtimeStatus(Status.PENDING) // 초기 상태
                .status(Status.ACTIVATED) // 기본 상태
                .isOvertimeRetroactive(requestDTO.getIsOvertimeRetroactive()) // 소급 여부
                .overtimeRetroactiveReason(requestDTO.getOvertimeRetroactiveReason()) // 소급 사유
                .overtimeRetroactiveStatus(Status.PENDING) // 소급 상태 초기화
                .build();
    }

    // List<Overtime> -> Employee Summary DTO
    public static WorkAttitudeOvertimeForEmployeeSummaryDTO toEmployeeSummaryDTO(String employeeName, List<Overtime> overtimes) {
        long nightHours = calculateTotalHoursByType(overtimes, OvertimeType.NIGHT);
        long holidayHours = calculateTotalHoursByType(overtimes, OvertimeType.HOLIDAY);
        long extendedHours = calculateTotalHoursByType(overtimes, OvertimeType.EXTENDED);
        long totalHours = nightHours + holidayHours + extendedHours;

        return WorkAttitudeOvertimeForEmployeeSummaryDTO.builder()
                .employeeName(employeeName) // 사원 이름
                .nightHours(nightHours) // 야간 근무 총 시간
                .holidayHours(holidayHours) // 휴일 근무 총 시간
                .extendedHours(extendedHours) // 연장 근무 총 시간
                .totalHours(totalHours) // 총 근무 시간
                .build();
    }

    // List<Overtime> -> Team Leader Summary DTO
    public static WorkAttitudeOvertimeForTeamLeaderSummaryDTO toTeamLeaderSummaryDTO(String employeeName, List<Overtime> overtimes) {
        long nightHours = calculateTotalHoursByType(overtimes, OvertimeType.NIGHT);
        long holidayHours = calculateTotalHoursByType(overtimes, OvertimeType.HOLIDAY);
        long extendedHours = calculateTotalHoursByType(overtimes, OvertimeType.EXTENDED);
        long totalHours = nightHours + holidayHours + extendedHours;

        return WorkAttitudeOvertimeForTeamLeaderSummaryDTO.builder()
                .employeeName(employeeName) // 사원 이름
                .nightHours(nightHours) // 야간 근무 총 시간
                .holidayHours(holidayHours) // 휴일 근무 총 시간
                .extendedHours(extendedHours) // 연장 근무 총 시간
                .totalHours(totalHours) // 총 근무 시간
                .build();
    }

    // 특정 유형의 총 시간 계산
    private static long calculateTotalHoursByType(List<Overtime> overtimes, OvertimeType type) {
        return overtimes.stream()
                .filter(overtime -> overtime.getOvertimeType() == type)
                .mapToLong(overtime -> Duration.between(overtime.getOvertimeStart(), overtime.getOvertimeEnd()).toHours())
                .sum();
    }
}
