package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
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
}
