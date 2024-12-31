package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.application.mapper.PositionMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.query.dto.PositionResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.PositionResponseListDTO;
import com.touchdown.perflowbackend.hr.query.repository.PositionQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionQueryService {

    private final PositionQueryRepository positionQueryRepository;

    @Transactional(readOnly = true)
    public PositionResponseListDTO getAllPosition(Pageable pageable) {

        Page<Position> pages = positionQueryRepository.findAll(pageable);

        List<PositionResponseDTO> positionList = PositionMapper.toResponse(pages.getContent());

        return PositionResponseListDTO.builder()
                .positions(positionList)
                .totalPages(pages.getTotalPages())
                .totalItems((int) pages.getTotalElements())
                .currentPage(pages.getNumber() + 1)
                .pageSize(pages.getSize())
                .build();
    }
}
