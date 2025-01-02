package com.touchdown.perflowbackend.file.query.controller;

import com.touchdown.perflowbackend.file.command.domain.aggregate.FileDirectory;
import com.touchdown.perflowbackend.file.query.dto.FileResponseDTO;
import com.touchdown.perflowbackend.file.query.service.FileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileQueryController {

    private final FileQueryService fileQueryService;

    @GetMapping("/announcement/{annId}")
    public ResponseEntity<List<FileResponseDTO>> getFilesByAnnouncement(@PathVariable Long annId) {

        return ResponseEntity.ok(fileQueryService.getFilesByDomainEntity(annId, FileDirectory.ANNOUNCEMENT));
    }

    @GetMapping("/doc/{docId}")
    public ResponseEntity<List<FileResponseDTO>> getFilesByDoc(@PathVariable Long docId) {

        return ResponseEntity.ok(fileQueryService.getFilesByDomainEntity(docId, FileDirectory.DOC));
    }
}
