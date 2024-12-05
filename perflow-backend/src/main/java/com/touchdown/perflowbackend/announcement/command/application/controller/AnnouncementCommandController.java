package com.touchdown.perflowbackend.announcement.command.application.controller;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.application.service.AnnouncementCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Announcement-Controller", description = "공지 관련 API")
@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementCommandController {

    private final AnnouncementCommandService announcementCommandService;

    @PostMapping
    public ResponseEntity<SuccessCode> createAnnouncement(@RequestBody AnnouncementRequestDTO announcementRequestDTO) {

        announcementCommandService.createAnnouncement(announcementRequestDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @PutMapping("/{annId}")
    public ResponseEntity<SuccessCode> updateAnnouncement(
            @PathVariable Long annId,
            @RequestBody AnnouncementRequestDTO announcementRequestDTO) {

        announcementCommandService.updateAnnouncement(annId, announcementRequestDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @DeleteMapping("/{annId}")
    public ResponseEntity<SuccessCode> deleteAnnouncement(@PathVariable Long annId, @RequestBody String empId) {

        // empId는 토큰에서 추출하는 로직으로 변경 예정
        announcementCommandService.deleteAnnouncement(annId, empId);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
