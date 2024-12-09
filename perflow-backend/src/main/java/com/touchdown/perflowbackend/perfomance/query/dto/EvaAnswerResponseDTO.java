package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class EvaAnswerResponseDTO {

    private final Long questionId;
    private final String answer;

}
