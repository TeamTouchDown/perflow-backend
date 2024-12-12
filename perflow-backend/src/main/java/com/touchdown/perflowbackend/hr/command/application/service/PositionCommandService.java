package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.mapper.PositionMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PositionCommandService {

    private final PositionCommandRepository positionCommandRepository;

    @Transactional
    public void createPosition(PositionCreateDTO positionCreateDTO) {

        Position position = PositionMapper.toEntity(positionCreateDTO);

        positionCommandRepository.save(position);
    }

    @Transactional
    public void updatePosition(PositionUpdateDTO positionUpdateDTO) {

        Position position = getPosition(positionUpdateDTO.getPositionId());

        position.updatePostion(positionUpdateDTO);

        positionCommandRepository.save(position);
    }

    private Position getPosition(Long positionId) {

        return positionCommandRepository.findById(positionId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POSITION)
        );
    }
}
