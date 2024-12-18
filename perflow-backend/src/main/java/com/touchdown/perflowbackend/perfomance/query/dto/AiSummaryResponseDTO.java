package com.touchdown.perflowbackend.perfomance.query.dto;

import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.PerfoType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AiSummaryResponseDTO {

    private final String summary;
    private final PerfoType perfoType;
}
