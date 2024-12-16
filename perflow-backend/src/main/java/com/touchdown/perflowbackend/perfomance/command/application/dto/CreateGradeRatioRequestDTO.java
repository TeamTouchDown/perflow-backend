package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateGradeRatioRequestDTO {

    private final Long sRatio;

    private final Long aRatio;

    private final Long bRatio;

    private final Long cRatio;

    private final Long dRatio;

    private final String reason;
}
