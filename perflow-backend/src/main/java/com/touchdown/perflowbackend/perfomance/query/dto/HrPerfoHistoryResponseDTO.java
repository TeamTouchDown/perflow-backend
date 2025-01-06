package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class HrPerfoHistoryResponseDTO {

    private final Long hrPerfoHistoryId;

    private final String perfoEmpId;

    private final String perfoedEmpId;

    private final Long AdjustmentDegree;

    private final Long AdjustmentColScore;

    private final Long AdjustmentDownScore;

    private final String AdjustmentReason;
}
