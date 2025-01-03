package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeTravelResponseDTO;

import java.time.LocalDateTime;

public class WorkAttitudeTravelMapper {

    public static Travel toEntity(WorkAttitudeTravelRequestDTO requestDTO, Employee employee, Employee approver) {
        // 시간 검증 추가
        if (requestDTO.getTravelEnd().isBefore(requestDTO.getTravelStart())) {
            throw new IllegalArgumentException("Travel end time cannot be before start time.");
        }

        return Travel.builder()
                .empId(employee) // Employee 객체 설정 (@ManyToOne 관계)
                .approver(approver) // ApproveSbj 객체 설정 (@ManyToOne 관계)
                .enrollTravel(requestDTO.getEnrollTravel() != null ? requestDTO.getEnrollTravel() : LocalDateTime.now()) // 신청 일자
                .travelReason(requestDTO.getTravelReason()) // 출장 사유
                .travelStart(requestDTO.getTravelStart()) // 출장 시작일
                .travelEnd(requestDTO.getTravelEnd()) // 출장 종료일
                .travelStatus(requestDTO.getTravelStatus() != null ? requestDTO.getTravelStatus() : Status.PENDING)
                .travelDivision(requestDTO.getTravelDivision()) // 출장 구분 (해외, 국내)
                .status(Status.ACTIVATED) // 기본 상태
                .travelRejectReason(null) // 초기에는 반려 사유 없음
                .build();
    }


    public static WorkAttitudeTravelResponseDTO toResponseDTO(Travel travel) {
        return WorkAttitudeTravelResponseDTO.builder()
                .travelId(travel.getTravelId())
                // 신청자
                .empId(travel.getEmployee().getEmpId())

                // 결재자
                .approverId(travel.getApprover().getEmpId())
                .approverName(travel.getApprover().getName())
                .travelReason(travel.getTravelReason())
                .travelStart(travel.getTravelStart())
                .travelEnd(travel.getTravelEnd())
                .travelStatus(travel.getTravelStatus().toString())
                .travelDivision(travel.getTravelDivision())
                .travelRejectReason(travel.getTravelRejectReason())
                .createDatetime(travel.getCreateDatetime())
                .updateDatetime(travel.getUpdateDatetime())
                .status(travel.getStatus())
                .build();
    }


}
