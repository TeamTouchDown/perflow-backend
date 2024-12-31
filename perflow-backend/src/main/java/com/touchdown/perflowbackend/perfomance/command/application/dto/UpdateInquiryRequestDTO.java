package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UpdateInquiryRequestDTO {

    private final String reason;
    private final Double score;
}
