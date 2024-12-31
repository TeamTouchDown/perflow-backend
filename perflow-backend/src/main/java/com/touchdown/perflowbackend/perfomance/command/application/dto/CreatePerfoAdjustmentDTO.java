package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreatePerfoAdjustmentDTO {

    private final Long degree;

    private final Long colScore;

    private final Long downScore;

    private final String reason;
}
