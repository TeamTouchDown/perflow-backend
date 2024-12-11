package com.touchdown.perflowbackend.announcement.command.application.controller;

import com.touchdown.perflowbackend.announcement.command.application.dto.AnnouncementRequestDTO;
import com.touchdown.perflowbackend.announcement.command.application.service.AnnouncementCommandService;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Announcement-Controller", description = "공지 관련 API")
@RestController
@RequestMapping("/api/v1/announcements")
@RequiredArgsConstructor
public class AnnouncementCommandController {

    private final AnnouncementCommandService announcementCommandService;

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SuccessCode> createAnnouncement(
            @RequestPart(value = "announcementRequestDTO") AnnouncementRequestDTO announcementRequestDTO,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) {

        announcementCommandService.createAnnouncement(announcementRequestDTO, files);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @PutMapping(value = "/{annId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuccessCode> updateAnnouncement(
            @PathVariable Long annId,
            @RequestPart(value = "announcementRequestDTO") AnnouncementRequestDTO announcementRequestDTO,
            @RequestPart(value = "addedFiles", required = false) List<MultipartFile> addedFiles,
            @RequestParam(value = "deletedFileIds", required = false) List<Long> deletedFileIds) {

        announcementCommandService.updateAnnouncement(annId, announcementRequestDTO, addedFiles, deletedFileIds);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @DeleteMapping("/{annId}")
    public ResponseEntity<SuccessCode> deleteAnnouncement(@PathVariable Long annId, @RequestBody String empId) {

        // empId는 추후 인증된 사용자 정보를 기반으로 추출
        announcementCommandService.deleteAnnouncement(annId, empId);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }
}
