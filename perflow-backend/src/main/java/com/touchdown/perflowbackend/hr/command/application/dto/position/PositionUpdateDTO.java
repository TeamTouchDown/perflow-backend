package com.touchdown.perflowbackend.hr.command.application.dto.position;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PositionUpdateDTO {

    private Long positionId;

    private String name;

    private Integer positionLevel;

    private Status status;
}
