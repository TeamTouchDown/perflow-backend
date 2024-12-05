package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeTravelMapper;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeTravelResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttributeTravelQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttitudeTravelQueryService {

    private final WorkAttributeTravelQueryRepository workAttributeTravelQueryRepository;

    @Transactional
    public WorkAttitudeTravelResponseDTO getTravelById(Long travelId) {
        Travel travel = workAttributeTravelQueryRepository.findById(travelId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TRAVEL));
        return WorkAttitudeTravelMapper.toResponseDTO(travel);
    }
}
