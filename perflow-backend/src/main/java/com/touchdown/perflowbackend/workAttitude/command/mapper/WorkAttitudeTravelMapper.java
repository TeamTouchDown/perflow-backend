package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;

import java.time.LocalDateTime;

public class WorkAttitudeTravelMapper {

    public static Travel toEntity(WorkAttitudeTravelRequestDTO requestDTO, Employee employee, ApproveSbj approveSbj) {
        return Travel.builder()
                .empId(employee) // Employee 객체 설정 (@ManyToOne 관계)
                .approveSbj(approveSbj) // ApproveSbj 객체 설정 (@ManyToOne 관계)
                .enrollTravel(requestDTO.getEnrollTravel()) // 신청 일자
                .travelReason(requestDTO.getTravelReason()) // 출장 사유
                .travelStart(requestDTO.getTravelStart()) // 출장 시작일
                .travelEnd(requestDTO.getTravelEnd()) // 출장 종료일
                .travelStatus(Status.valueOf("PENDING")) // 초기 상태
                .travelDivision(requestDTO.getTravelDivision()) // 출장 구분 (해외, 국내)
                .status(Status.valueOf("ACTIVE")) // 기본 상태
                .travelRejectReason(requestDTO.getTravelRejectReason()) // 반려 사유
                .build();
    }
}
