package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UpdateQuestionRequestDTO {

    private final String questionContext;

    private final String questionType;
}
