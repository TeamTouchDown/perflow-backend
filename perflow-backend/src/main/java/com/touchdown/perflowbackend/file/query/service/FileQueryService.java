package com.touchdown.perflowbackend.file.query.service;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.announcement.query.repository.AnnouncementQueryRepository;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.query.repository.DocQueryRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.file.command.domain.aggregate.File;
import com.touchdown.perflowbackend.file.command.domain.aggregate.FileDirectory;
import com.touchdown.perflowbackend.file.query.dto.FileResponseDTO;
import com.touchdown.perflowbackend.file.query.repository.FileQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileQueryService {

    private final FileQueryRepository fileQueryRepository;
    private final DocQueryRepository docQueryRepository;
    private final AnnouncementQueryRepository announcementQueryRepository;

    public List<FileResponseDTO> getFilesByDomainEntity(Long domainEntityId, FileDirectory fileDirectory) {
        List<File> files;

        if (fileDirectory.equals(FileDirectory.DOC)) {
            Doc doc = docQueryRepository.findById(domainEntityId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DOC));

            files = fileQueryRepository.findByDoc(doc);
        } else if (fileDirectory.equals(FileDirectory.ANNOUNCEMENT)) {
            Announcement announcement = announcementQueryRepository.findById(domainEntityId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ANNOUNCEMENT));

            files = fileQueryRepository.findByAnn(announcement);
        } else {
            throw new CustomException(ErrorCode.UNSUPPORTED_ENTITY);
        }

        return files.stream().map(this::toFileResponseDTO).toList();
    }

    private FileResponseDTO toFileResponseDTO(File file) {
        FileResponseDTO dto = new FileResponseDTO();

        dto.setId(file.getFileId());
        dto.setOriginName(file.getOriginName());
        dto.setFileName(file.getFileName());
        dto.setType(file.getType());
        dto.setSize(file.getSize());
        dto.setUrl(file.getUrl());

        return dto;
    }
}
