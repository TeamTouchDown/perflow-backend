package com.touchdown.perflowbackend.file.command.application.controller;

import com.touchdown.perflowbackend.file.command.application.service.FileService;
import com.touchdown.perflowbackend.file.command.domain.aggregate.File;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileCommandController {

    private final FileService fileService;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long fileId) {
        File file = fileService.findFileById(fileId);

        // S3에서 파일 데이터 읽기
        byte[] fileData = fileService.downloadFromS3(file);

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(file.getOriginName())
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(fileData);
    }
}
