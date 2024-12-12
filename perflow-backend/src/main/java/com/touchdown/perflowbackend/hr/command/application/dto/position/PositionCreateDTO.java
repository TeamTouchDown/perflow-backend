package com.touchdown.perflowbackend.hr.command.application.dto.position;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PositionCreateDTO {

    private Long positionId;

    private String name;

    private Integer positionLevel;
}
