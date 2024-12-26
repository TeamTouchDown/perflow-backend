package com.touchdown.perflowbackend.announcement.query.service;

import com.touchdown.perflowbackend.announcement.command.mapper.AnnouncementMapper;
import com.touchdown.perflowbackend.announcement.query.dto.AnnouncementResponseDTO;
import com.touchdown.perflowbackend.announcement.query.repository.AnnouncementQueryRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementQueryService {

    private final AnnouncementQueryRepository announcementQueryRepository;

    @Transactional(readOnly = true)
    public Page<AnnouncementResponseDTO> readAll(Pageable pageable) {

        return announcementQueryRepository.findAll(pageable)
                .map(AnnouncementMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public AnnouncementResponseDTO readOne(Long annId) {

        return announcementQueryRepository.findById(annId)
                .map(AnnouncementMapper::toDTO)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANNOUNCEMENT));
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementResponseDTO> searchAnnouncementsByTitle(String title, Pageable pageable) {

        return announcementQueryRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(AnnouncementMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public Page<AnnouncementResponseDTO> getAnnouncementByDeptName(String deptName, Pageable pageable) {

        return announcementQueryRepository.findByDeptName(deptName, pageable)
                .map(AnnouncementMapper::toDTO);
    }
}
