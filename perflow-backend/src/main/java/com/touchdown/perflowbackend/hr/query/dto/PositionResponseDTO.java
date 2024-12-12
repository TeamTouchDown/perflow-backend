package com.touchdown.perflowbackend.hr.query.dto;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PositionResponseDTO {

    private Long positionId;

    private String name;

    private Integer positionLevel;

    private Status status;
}
