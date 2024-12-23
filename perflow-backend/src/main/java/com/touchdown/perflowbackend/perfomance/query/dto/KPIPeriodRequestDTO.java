package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KPIPeriodRequestDTO {

    private final Integer year;

    private final Integer quarter;

    private final Integer month;
}
