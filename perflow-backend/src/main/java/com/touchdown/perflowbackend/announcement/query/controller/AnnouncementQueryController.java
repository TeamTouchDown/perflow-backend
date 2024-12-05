package com.touchdown.perflowbackend.announcement.query.controller;

import com.touchdown.perflowbackend.announcement.query.dto.AnnouncementResponseDTO;
import com.touchdown.perflowbackend.announcement.query.service.AnnouncementQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Announcement-Controller", description = "공지 관련 API")
@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementQueryController {

    private final AnnouncementQueryService announcementQueryService;

    @GetMapping
    public ResponseEntity<Page<AnnouncementResponseDTO>> readAll(Pageable pageable) {

        return ResponseEntity.ok(announcementQueryService.readAll(pageable));
    }

    @GetMapping("/{annId}")
    public ResponseEntity<AnnouncementResponseDTO> readOne(@PathVariable Long annId) {

        return ResponseEntity.ok(announcementQueryService.readOne(annId));
    }
}
