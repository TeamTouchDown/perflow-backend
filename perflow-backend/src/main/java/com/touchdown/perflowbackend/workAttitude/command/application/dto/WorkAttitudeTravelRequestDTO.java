package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import lombok.Data;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeTravelRequestDTO {


    private String approverId;

    private LocalDateTime enrollTravel = LocalDateTime.now();

    private String travelReason;

    private LocalDateTime travelStart;

    private LocalDateTime travelEnd;

    private String travelDivision;

    private Status travelStatus = Status.PENDING;

    private String travelRejectReason = null;

}
