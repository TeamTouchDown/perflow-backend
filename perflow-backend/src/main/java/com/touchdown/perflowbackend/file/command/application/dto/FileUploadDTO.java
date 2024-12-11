package com.touchdown.perflowbackend.file.command.application.dto;

import com.touchdown.perflowbackend.file.command.domain.aggregate.FileDirectory;
import com.touchdown.perflowbackend.file.command.domain.aggregate.FileType;
import lombok.Data;

@Data
public class FileUploadDTO {

    private Long id;

    private String originName;

    private String fileName;

    private FileType type;

    private FileDirectory fileDirectory;

    private Integer size;
}
