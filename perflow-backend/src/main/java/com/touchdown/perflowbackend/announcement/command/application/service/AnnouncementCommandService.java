package com.touchdown.perflowbackend.announcement.command.application.service;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.domain.repository.AnnouncementCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AnnouncementCommandService {

    private final AnnouncementCommandRepository announcementCommandRepository;

    @Transactional
    public Long createAnnouncement(AnnouncementRequestDTO announcementRequestDTO) {


        return null;
    }
}
