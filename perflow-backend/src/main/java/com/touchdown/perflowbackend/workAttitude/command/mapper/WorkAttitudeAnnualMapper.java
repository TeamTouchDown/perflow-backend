package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAnnualRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualRetroactiveStatus;

public class WorkAttitudeAnnualMapper {

    // DTO -> Entity 변환
    public static Annual toEntity(WorkAttitudeAnnualRequestDTO requestDTO, Employee employee, ApproveSbj approveSbj) {
        return Annual.builder()
                .empId(employee) // 사원 정보
                .approveSbjId(approveSbj) // 승인 정보
                .enrollAnnual(requestDTO.getEnrollAnnual()) // 연차 등록일
                .annualStart(requestDTO.getAnnualStart()) // 연차 시작일
                .annualEnd(requestDTO.getAnnualEnd()) // 연차 종료일
                .annualType(requestDTO.getAnnualType()) // 연차 유형
                .annualRejectReason(requestDTO.getAnnualRejectReason()) // 반려 사유
                .isAnnualRetroactive(requestDTO.getIsAnnualRetroactive()) // 소급 여부
                .annualRetroactiveReason(requestDTO.getAnnualRetroactiveReason()) // 소급 사유
                .annualRetroactiveStatus(requestDTO.getAnnualRetroactiveStatus()) // 소급 상태
                .status(requestDTO.getStatus() != null ? requestDTO.getStatus() : Status.ACTIVATED) // 상태
                .build();
    }

    // DTO -> 기존 Entity 업데이트
    public static void updateEntityFromDto(WorkAttitudeAnnualRequestDTO requestDTO, Annual annual) {
        annual.updateAnnual(
                requestDTO.getAnnualStart(),
                requestDTO.getAnnualEnd(),
                requestDTO.getAnnualType()
        ); // 연차 기본 정보 업데이트

        if (requestDTO.getAnnualRejectReason() != null) {
            annual.rejectAnnual(requestDTO.getAnnualRejectReason()); // 반려 사유 업데이트
        }

        annual.updateRetroactive(
                requestDTO.getIsAnnualRetroactive(),
                requestDTO.getAnnualRetroactiveReason(),
                requestDTO.getAnnualRetroactiveStatus()
        ); // 소급 관련 정보 업데이트
    }
}
