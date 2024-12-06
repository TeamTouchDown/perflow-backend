package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KPIDetailRequestDTO {

    private final String goal;

    private final Long goalValue;

    private final String goalValueUnit;

    private final String goalDetail;
}
