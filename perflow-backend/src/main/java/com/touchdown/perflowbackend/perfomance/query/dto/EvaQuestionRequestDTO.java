package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EvaQuestionRequestDTO {

    private final Long deptId;
    private final String questionType;
    private final String perfoType;
}
