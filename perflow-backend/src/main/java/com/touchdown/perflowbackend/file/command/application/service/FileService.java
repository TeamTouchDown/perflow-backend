package com.touchdown.perflowbackend.file.command.application.service;

import com.touchdown.perflowbackend.announcement.command.domain.aggregate.Announcement;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.file.command.application.dto.FileUploadDTO;
import com.touchdown.perflowbackend.file.command.domain.aggregate.File;
import com.touchdown.perflowbackend.file.command.domain.aggregate.FileDirectory;
import com.touchdown.perflowbackend.file.command.domain.aggregate.FileType;
import com.touchdown.perflowbackend.file.command.domain.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    private final S3Client s3Client;
    private final FileRepository fileRepository;

    private static final String ROOT_DIR = "files/";
    private static final String DOC_DIR = "docs/";
    private static final String ANN_DIR = "announcements/";

    @Value("${AWS_BUCKET_NAME}")
    private String bucketName;

    // 여러 파일 업로드 메서드
    public List<FileUploadDTO> uploadFiles(List<MultipartFile> multipartFiles, FileDirectory directory, Object domainEntity) {
        if (multipartFiles == null || multipartFiles.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXIST_UPLOAD_FILE);
        }

        return multipartFiles.stream()
                .map(file -> uploadFile(file, directory, domainEntity))
                .toList();
    }

    // 단일 파일 업로드 메서드
    public FileUploadDTO uploadFile(MultipartFile multipartFile, FileDirectory directory, Object domainEntity) {
        validateFileExtension(multipartFile.getOriginalFilename());

        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = generateFileName(originalFileName);
        String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/files/" + directory.toString().toLowerCase() + "s/" + fileName;

        // S3에 파일 업로드
        uploadToS3(multipartFile, fileName, directory);

        // DB에 파일 정보 저장
        File file = saveFileInfo(originalFileName, fileName, fileUrl, multipartFile.getSize(), domainEntity);

        // FileUploadDTO 생성 후 반환
        return toFileUploadDTO(file);
    }

    public String uploadSeal(MultipartFile multipartFile) {

        String originalFileName = multipartFile.getOriginalFilename();
        String fileName = generateFileName(originalFileName);
        String fileUrl = "https://" + bucketName + ".s3.ap-northeast-2.amazonaws.com/files/seals/" + fileName;

        uploadToS3(multipartFile, fileName, FileDirectory.SEAL);

        return fileUrl;
    }

    // 파일 삭제 메서드
    public void deleteFile(File file) {
        // S3에서 파일 삭제
        deleteFileFromS3(file);

        // DB에서 파일 삭제
        fileRepository.delete(file);
    }

    // 여러 파일 삭제 메서드
    public void deleteFilesByDomainEntity(Object domainEntity) {
        List<File> files;

        if (domainEntity instanceof Doc) {
            files = fileRepository.findByDoc((Doc) domainEntity);
        } else if (domainEntity instanceof Announcement) {
            files = fileRepository.findByAnn((Announcement) domainEntity);
        } else {
            throw new CustomException(ErrorCode.UNSUPPORTED_ENTITY);
        }

        files.forEach(this::deleteFile);
    }

    public File findFileById(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_FILE));
    }

    private void deleteFileFromS3(File file) {

        String s3Key = generateS3Key(file);

        try {
            s3Client.deleteObject(b -> b.bucket(bucketName).key(s3Key));
            log.info("Deleted file from S3: {}", s3Key);
        } catch (Exception e) {
            log.error("Failed to delete file from S3: {}", s3Key, e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 파일 정보 저장
    private File saveFileInfo(String originName, String fileName, String url, long size, Object domainEntity) {
        File.FileBuilder fileBuilder = File.builder()
                .originName(originName)
                .fileName(fileName)
                .url(url)
                .size((int) size)
                .type(FileType.valueOf(getFileExtension(originName).toUpperCase()));

        // Domain 연관 설정
        if (domainEntity instanceof Doc) {
            fileBuilder.doc((Doc) domainEntity);
        } else if (domainEntity instanceof Announcement) {
            fileBuilder.ann((Announcement) domainEntity);
        } else {
            throw new CustomException(ErrorCode.UNSUPPORTED_ENTITY);
        }

        File file = fileBuilder.build();
        return fileRepository.save(file);
    }

    // DTO 변환
    private FileUploadDTO toFileUploadDTO(File file) {
        FileUploadDTO dto = new FileUploadDTO();
        dto.setId(file.getDoc() != null ? file.getDoc().getDocId() : file.getAnn().getAnnId());
        dto.setOriginName(file.getOriginName());
        dto.setFileName(file.getFileName());
        dto.setType(file.getType());
        dto.setSize(file.getSize());
        return dto;
    }

    // 파일 확장자 검증
    private void validateFileExtension(String originalFileName) {
        if (!FileType.isValidExtension(originalFileName)) {
            throw new CustomException(ErrorCode.UNSUPPORTED_FILE_EXTENSION);
        }
    }

    // 파일 이름 생성
    private String generateFileName(String originalFileName) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        return UUID.randomUUID() + extension;
    }

    // S3 업로드
    private void uploadToS3(MultipartFile multipartFile, String fileName, FileDirectory directory) {

        String dirPath = "files/" + directory.toString().toLowerCase() + "s/";
        String s3Key = dirPath + fileName;

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(s3Key)
                            .contentType(multipartFile.getContentType())
                            .build(),
                    RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize())
            );
        } catch (IOException e) {
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    // 파일 확장자 추출
    private String getFileExtension(String fileName) {

        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private String generateS3Key(File file) {

        String directory;

        if (file.getDoc() != null) {
            directory = DOC_DIR;
        } else if (file.getAnn() != null) {
            directory = ANN_DIR;
        } else {
            throw new CustomException(ErrorCode.UNSUPPORTED_ENTITY);
        }

        return ROOT_DIR + directory + file.getFileName();
    }
}
