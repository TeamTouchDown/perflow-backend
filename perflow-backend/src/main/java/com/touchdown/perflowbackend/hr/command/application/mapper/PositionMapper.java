package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;

public class PositionMapper {

    public static Position toEntity(PositionCreateDTO positionCreateDTO) {

        return Position.builder()
                .positionCreateDTO(positionCreateDTO)
                .build();
    }
}
