package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KPIDetailResponseDTO {

    private final String empId;
    private final String goal;
    private final Long goalValue;
    private final String goalValueUnit;
    private final String goalDetail;
    private final Double currentValue;

    public KPIDetailResponseDTO(String empId, String goal, Long goalValue, String goalValueUnit, String goalDetail, double currentValue) {

        this.empId = empId;
        this.goal = goal;
        this.goalValue = goalValue;
        this.goalValueUnit = goalValueUnit;
        this.goalDetail = goalDetail;
        this.currentValue = currentValue;
    }
}


