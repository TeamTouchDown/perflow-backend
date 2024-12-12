package com.touchdown.perflowbackend.hr.command.application.dto.position;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionUpdateDTO {

    private Long positionId;

    private String name;

    private Integer positionLevel;
}
