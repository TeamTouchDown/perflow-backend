package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeVacationRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Vacation;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.VacationStatus;

import java.time.LocalDateTime;

public class WorkAttitudeVacationMapper {

    // DTO -> Entity 변환
    public static Vacation toEntity(WorkAttitudeVacationRequestDTO requestDTO, Employee employee, Employee approver) {
        return Vacation.builder()
                .empId(employee) // 사원 정보
                .approver(approver) // 승인 정보
                .enrollVacation(requestDTO.getEnrollVacation() != null ? requestDTO.getEnrollVacation() : LocalDateTime.now()) // 휴가 등록일
                .vacationStart(requestDTO.getVacationStart()) // 휴가 시작일
                .vacationEnd(requestDTO.getVacationEnd()) // 휴가 종료일
                .vacationType(requestDTO.getVacationType()) // 휴가 유형
                .vacationRejectReason(requestDTO.getVacationRejectReason()) // 반려 사유
                .vacationStatus(requestDTO.getVacationStatus() != null ? requestDTO.getVacationStatus() : VacationStatus.PENDING) // 휴가 상태
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : Status.ACTIVATED) // 기본 상태 (활성화)
                .build();
    }

    // DTO -> 기존 Entity 업데이트
    public static void updateEntityFromDto(WorkAttitudeVacationRequestDTO requestDTO, Vacation vacation) {
        vacation.updateVacation(
                requestDTO.getVacationStart(),
                requestDTO.getVacationEnd(),
                requestDTO.getVacationType()
        ); // 휴가 기본 정보 업데이트

        if (requestDTO.getVacationRejectReason() != null) {
            vacation.rejectVacation(requestDTO.getVacationRejectReason()); // 반려 사유 업데이트
        }
    }
}
