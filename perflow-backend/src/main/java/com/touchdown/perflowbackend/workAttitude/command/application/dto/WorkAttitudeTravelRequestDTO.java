package com.touchdown.perflowbackend.workAttitude.command.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class WorkAttitudeTravelRequestDTO {


    private String approverId;

    private LocalDateTime enrollTravel;

    private String travelReason;

    private LocalDateTime travelStart;

    private LocalDateTime travelEnd;

    private String travelDivision;

}
