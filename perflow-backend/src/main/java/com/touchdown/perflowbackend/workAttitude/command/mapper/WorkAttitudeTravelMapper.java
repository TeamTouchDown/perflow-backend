package com.touchdown.perflowbackend.workAttitude.command.mapper;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;

import java.time.LocalDateTime;

public class WorkAttitudeTravelMapper {
    // DTO → 엔터티 변환
    public static Travel toEntity(WorkAttitudeTravelRequestDTO requestDTO, Employee employee ) {
        return Travel.builder()
                .empId(employee)
                .travelReason(requestDTO.getTravelReason())
                .travelStart(requestDTO.getTravelStart())
                .travelEnd(requestDTO.getTravelEnd())
                .travelDivision(requestDTO.getTravelDivision())
                .travelStatus("PENDING") // 초기 상태 설정
                .createdAt(LocalDateTime.now()) // 생성 시간
                .build();
    }
}
