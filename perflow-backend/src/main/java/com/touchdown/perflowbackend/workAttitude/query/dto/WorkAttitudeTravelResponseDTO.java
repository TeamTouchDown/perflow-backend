package com.touchdown.perflowbackend.workAttitude.query.dto;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class WorkAttitudeTravelResponseDTO {

    private Long travelId;

    private String empId;

    private String approverId;

    private String approverName;

    private String travelReason;

    private LocalDateTime travelStart;

    private LocalDateTime travelEnd;

    private String travelStatus;    // PENDING, CONFIRMED 등

    private String travelDivision;

    private String travelRejectReason;

    private LocalDateTime createDatetime;

    private LocalDateTime updateDatetime;

    private Status status;          // 소프트 딜리트 등
}

