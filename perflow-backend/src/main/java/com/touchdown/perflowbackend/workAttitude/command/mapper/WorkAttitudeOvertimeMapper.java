package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeOvertimeForEmployeeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeOvertimeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForEmployeeSummaryDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeForTeamLeaderSummaryDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class WorkAttitudeOvertimeMapper {

    public static Overtime toEntity(WorkAttitudeOvertimeRequestDTO requestDTO, Employee employee, Employee approver) {
        return Overtime.builder()
                .empId(employee) // 사원 정보
                .approver(approver) // 승인자 정보
                .overtimeType(requestDTO.getOvertimeType()) // 초과 근무 유형
                .enrollOvertime(requestDTO.getEnrollOvertime() != null ? requestDTO.getEnrollOvertime() : LocalDateTime.now())
                .overtimeStart(requestDTO.getOvertimeStart()) // 초과 근무 시작 시간
                .overtimeEnd(requestDTO.getOvertimeEnd()) // 초과 근무 종료 시간
                .overtimeStatus(Status.PENDING) // 초기 상태
                .status(Status.ACTIVATED) // 기본 상태
                .isOvertimeRetroactive(requestDTO.getIsOvertimeRetroactive() != null ? requestDTO.getIsOvertimeRetroactive() : false) // 소급 여부
                .overtimeRetroactiveReason(requestDTO.getOvertimeRetroactiveReason()!= null ? requestDTO.getOvertimeRetroactiveReason() : "") // 소급 사유
                .overtimeRetroactiveStatus(Status.PENDING) // 소급 상태 초기화
                .build();
    }

    // 시간 계산 요약 메서드 (중복 제거)
    private static WorkAttitudeOvertimeForEmployeeSummaryDTO buildEmployeeSummary(String employeeName, List<Overtime> overtimes) {
        long nightHours = calculateTotalHoursByType(overtimes, OvertimeType.NIGHT);
        long holidayHours = calculateTotalHoursByType(overtimes, OvertimeType.HOLIDAY);
        long extendedHours = calculateTotalHoursByType(overtimes, OvertimeType.EXTENDED);
        long totalHours = nightHours + holidayHours + extendedHours;

        return WorkAttitudeOvertimeForEmployeeSummaryDTO.builder()
                .employeeName(employeeName)
                .nightHours(nightHours)
                .holidayHours(holidayHours)
                .extendedHours(extendedHours)
                .totalHours(totalHours)
                .build();
    }

    // Employee Summary DTO 변환
    public static WorkAttitudeOvertimeForEmployeeSummaryDTO toEmployeeSummaryDTO(String employeeName, List<Overtime> overtimes) {
        if (overtimes == null || overtimes.isEmpty()) {
            return buildEmployeeSummary(employeeName, List.of());
        }
        return buildEmployeeSummary(employeeName, overtimes);
    }

    // Team Leader Summary DTO 변환
    public static WorkAttitudeOvertimeForTeamLeaderSummaryDTO toTeamLeaderSummaryDTO(String employeeName, List<Overtime> overtimes) {
        if (overtimes == null || overtimes.isEmpty()) {
            return WorkAttitudeOvertimeForTeamLeaderSummaryDTO.builder()
                    .employeeName(employeeName)
                    .nightHours(0)
                    .holidayHours(0)
                    .extendedHours(0)
                    .totalHours(0)
                    .build();
        }
        long nightHours = calculateTotalHoursByType(overtimes, OvertimeType.NIGHT);
        long holidayHours = calculateTotalHoursByType(overtimes, OvertimeType.HOLIDAY);
        long extendedHours = calculateTotalHoursByType(overtimes, OvertimeType.EXTENDED);
        long totalHours = nightHours + holidayHours + extendedHours;

        return WorkAttitudeOvertimeForTeamLeaderSummaryDTO.builder()
                .employeeName(employeeName)
                .nightHours(nightHours)
                .holidayHours(holidayHours)
                .extendedHours(extendedHours)
                .totalHours(totalHours)
                .build();
    }

    // 특정 유형의 총 시간 계산
    private static long calculateTotalHoursByType(List<Overtime> overtimes, OvertimeType type) {
        if (overtimes == null || overtimes.isEmpty()) {
            return 0;
        }

        return overtimes.stream()
                .filter(overtime -> overtime.getOvertimeType() == type)
                .mapToLong(overtime -> {
                    if (overtime.getOvertimeEnd() == null || overtime.getOvertimeStart() == null) {
                        throw new IllegalArgumentException("초과근무 시작 종료 시간을 입력하세요");
                    }

                    return Duration.between(overtime.getOvertimeStart(), overtime.getOvertimeEnd()).toHours();
                })
                .sum();
    }
}
