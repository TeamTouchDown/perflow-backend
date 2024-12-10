package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttributeOvertimeForEmployeeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttributeOvertimeForEmployeeResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttributeOvertimeForTeamLeaderResponseDTO;

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

    public static WorkAttributeOvertimeForEmployeeResponseDTO toResponseDTO(Overtime overtime){
        return WorkAttributeOvertimeForEmployeeResponseDTO.builder()
                .overtimeId(overtime.getOvertimeId())
                .overTimeType(overtime.getOvertimeType())
                .overtimeStart(overtime.getOvertimeStart())
                .overtimeEnd(overtime.getEnrollOvertime())
                .overtimeStatus(overtime.getOvertimeStatus())
                .enrollOvertime(overtime.getEnrollOvertime())
                .isOvertimeRetroactive(overtime.getIsOvertimeRetroactive())
                .overtimeRetroactiveReason(overtime.getOvertimeRetroactiveReason())
                .build();
    }

    public static WorkAttributeOvertimeForTeamLeaderResponseDTO toLeaderResponseDTO(Overtime overtime){
        return WorkAttributeOvertimeForTeamLeaderResponseDTO.builder()
                .overtimeId(overtime.getOvertimeId())
                .empId(overtime.getEmpId().getEmpId()) // 사원 ID
                .employeeName(overtime.getEmpId().getName()) // 사원 이름
                .overTimeType(overtime.getOvertimeType()) // 초과근무 유형
                .overtimeStart(overtime.getOvertimeStart()) // 초과근무 시작 시간
                .overtimeEnd(overtime.getOvertimeEnd()) // 초과근무 종료 시간
                .overtimeStatus(overtime.getOvertimeStatus()) // 초과근무 상태
                .isOvertimeRetroactive(overtime.getIsOvertimeRetroactive()) // 소급 여부
                .overtimeRetroactiveReason(overtime.getOvertimeRetroactiveReason()) // 소급 사유
                .approveSbjId(overtime.getApproveSbjId().getApproveSbjId()) // 승인자 이름
                .enrollOvertime(overtime.getEnrollOvertime()) // 신청 일자
                .createDatetime(overtime.getCreateDatetime()) // 생성 시간
                .updateDatetime(overtime.getUpdateDatetime()) // 수정 시간
                .build();
    }
}
