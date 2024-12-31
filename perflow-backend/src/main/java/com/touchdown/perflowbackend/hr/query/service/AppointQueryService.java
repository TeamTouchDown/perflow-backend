package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.application.mapper.AppointMapper;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;
import com.touchdown.perflowbackend.hr.query.dto.AppointResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.AppointResponseListDTO;
import com.touchdown.perflowbackend.hr.query.repository.AppointQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointQueryService {

    private final AppointQueryRepository appointQueryRepository;

    public AppointResponseListDTO getAppointList(Pageable pageable) {

        Page<Appoint> pages = appointQueryRepository.findAll(pageable);

        List<AppointResponseDTO> appointResponseDTOs = AppointMapper.toResponseDTO(pages.getContent());

        return AppointResponseListDTO.builder()
                .appointResponseList(appointResponseDTOs)
                .totalPages(pages.getTotalPages())
                .totalItems((int) pages.getTotalElements())
                .currentPage(pages.getNumber() + 1)
                .pageSize(pages.getSize())
                .build();
    }
}
