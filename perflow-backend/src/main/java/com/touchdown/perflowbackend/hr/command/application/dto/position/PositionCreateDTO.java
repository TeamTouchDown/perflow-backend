package com.touchdown.perflowbackend.hr.command.application.dto.position;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PositionCreateDTO {

    private String name;

    private Integer positionLevel;
}
