package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttributeOvertimeForEmployeeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;

public class WorkAttributeOvertimeMapper {

    public static Overtime toEntity(WorkAttributeOvertimeForEmployeeRequestDTO requestDTO, Employee employee, ApproveSbj approveSbj){
        return Overtime.builder()
                .empId(employee)//.empId(employee)
                .approveSbjId(approveSbj)//.approveSbjId(approveSbj) // ApproveSbj 객체 설정 (@ManyToOne 관계)
                .overtimeType(requestDTO.getOvertimeType()) // 초과 근무 타입
                .enrollOvertime(requestDTO.getEnrollOvertime()) // 신청 일자
                .overtimeStart(requestDTO.getOvertimeStart()) // 초과 근무 시작 시간
                .overtimeEnd(requestDTO.getOvertimeEnd()) // 초과 근무 종료 시간
                .overtimeStatus(Status.PENDING) // 초기 상태 설정 (대기)
                .status(Status.ACTIVATED) // 기본 상태 설정 (활성화)
                .isOvertimeRetroactive(requestDTO.getIsOvertimeRetroactive()) // 소급 여부 설정
                .overtimeRetroactiveReason(requestDTO.getOvertimeRetroactiveReason()) // 소급 사유 설정
                .overtimeRetroactiveStatus(Status.PENDING) // 소급 상태 초기화
                .build();

    }













}
