package com.touchdown.perflowbackend.workAttitude.query.dto;

import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class WorkAttitudeTravelResponseDTO {

    private Long travelId;

    private String empId;

    private Long approveSbjId;

    private String travelReason;

    private LocalDateTime travelStart;

    private LocalDateTime travelEnd;

    private String travelStatus;

    private String travelDivision;

    @Nullable
    private String travelRejectReason;

    private LocalDateTime createDatetime; // 생성 일시

    @Nullable
    private LocalDateTime updateDatetime; // 수정 일시

    private String status;




}
