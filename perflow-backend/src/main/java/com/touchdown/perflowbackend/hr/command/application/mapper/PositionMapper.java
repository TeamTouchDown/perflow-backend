package com.touchdown.perflowbackend.hr.command.application.mapper;

import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.query.dto.PositionResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class PositionMapper {

    public static Position toEntity(PositionCreateDTO positionCreateDTO) {

        return Position.builder()
                .positionCreateDTO(positionCreateDTO)
                .build();
    }

    public static List<PositionResponseDTO> toResponse(List<Position> positions) {

        List<PositionResponseDTO> positionResponseDTOS = new ArrayList<>();

        for (Position position : positions) {
            positionResponseDTOS.add(PositionResponseDTO.builder()
                    .positionId(position.getPositionId())
                    .name(position.getName())
                    .positionLevel(position.getPositionLevel())
                    .status(position.getStatus())
                    .build());
        }

        return positionResponseDTOS;
    }
}
