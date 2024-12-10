package com.touchdown.perflowbackend.perfomance.command.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateQuestionRequestDTO {

    private final Long deptId;
    private final String questionType;
    private final String questionContent;
    private final String perfoType;
}
