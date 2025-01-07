package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateInquiryRequestDTO {

    private final String reason;
    private final Double score;
}
