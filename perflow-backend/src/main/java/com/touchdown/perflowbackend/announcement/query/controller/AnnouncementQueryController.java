package com.touchdown.perflowbackend.announcement.query.controller;

import com.touchdown.perflowbackend.announcement.query.dto.AnnouncementResponseDTO;
import com.touchdown.perflowbackend.announcement.query.service.AnnouncementQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Announcement-Controller", description = "공지 관련 API")
@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementQueryController {

    private final AnnouncementQueryService announcementQueryService;

    @GetMapping
    public ResponseEntity<Page<AnnouncementResponseDTO>> readAll(@RequestParam(value = "deptName", required = false) String deptName, Pageable pageable) {

        if (deptName != null) {
            return ResponseEntity.ok(announcementQueryService.getAnnouncementByDeptName(deptName, pageable));
        }

        return ResponseEntity.ok(announcementQueryService.readAll(pageable));
    }

    @GetMapping("/{annId}")
    public ResponseEntity<AnnouncementResponseDTO> readOne(@PathVariable Long annId) {

        return ResponseEntity.ok(announcementQueryService.readOne(annId));
    }

    @GetMapping("/search/title")
    public ResponseEntity<Page<AnnouncementResponseDTO>> searchAnnouncementsByTitle(
            @RequestParam String title,
            Pageable pageable
    ) {

        return ResponseEntity.ok(announcementQueryService.searchAnnouncementsByTitle(title, pageable));
    }
}
