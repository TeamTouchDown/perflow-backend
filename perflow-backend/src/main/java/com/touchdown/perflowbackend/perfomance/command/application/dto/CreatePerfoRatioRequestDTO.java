package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreatePerfoRatioRequestDTO {

    private final Long personalKpiWeight;

    private final Long teamKpiWeight;

    private final Long colWeight;

    private final Long downWeight;

    private final Long attendanceWeight;

    private final String reason;
}
