package com.touchdown.perflowbackend.perfomance.query.dto;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.KpiCurrentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KPIDetailResponseDTO {

    private final Long kpiId;

    private final String empId;

    private final String goal;

    private final Long goalValue;

    private final String goalValueUnit;

    private final String goalDetail;

    private final Double currentValue;

    private final KpiCurrentStatus kpiCurrentStatus;

    public KPIDetailResponseDTO(Long kpiId, String empId, String goal, Long goalValue, String goalValueUnit, String goalDetail, double currentValue, KpiCurrentStatus status) {

        this.kpiId = kpiId;
        this.empId = empId;
        this.goal = goal;
        this.goalValue = goalValue;
        this.goalValueUnit = goalValueUnit;
        this.goalDetail = goalDetail;
        this.currentValue = currentValue;
        this.kpiCurrentStatus = status;
    }
}


