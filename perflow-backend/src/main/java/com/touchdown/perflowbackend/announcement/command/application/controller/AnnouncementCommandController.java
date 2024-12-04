package com.touchdown.perflowbackend.announcement.command.application.controller;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.application.service.AnnouncementCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementCommandController {

    private final AnnouncementCommandService announcementCommandService;

    @PostMapping
    public ResponseEntity<String> createAnnouncement(@RequestBody AnnouncementRequestDTO announcementRequestDTO) {

        announcementCommandService.createAnnouncement(announcementRequestDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS.getMessage());
    }
}
