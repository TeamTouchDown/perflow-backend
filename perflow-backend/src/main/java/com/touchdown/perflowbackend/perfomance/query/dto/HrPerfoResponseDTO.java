package com.touchdown.perflowbackend.perfomance.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class HrPerfoResponseDTO {

    private final String grade;

    private final String review;

    private final Long year;
}
