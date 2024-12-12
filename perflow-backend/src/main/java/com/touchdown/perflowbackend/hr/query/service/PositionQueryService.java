package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.application.mapper.PositionMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.query.dto.PositionResponseDTO;
import com.touchdown.perflowbackend.hr.query.repository.PositionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionQueryService {

    private final PositionQueryRepository positionQueryRepository;

    @Transactional(readOnly = true)
    public List<PositionResponseDTO> getAllPosition() {

        List<Position> positions = positionQueryRepository.findAll();

        return PositionMapper.toResponse(positions);
    }
}
