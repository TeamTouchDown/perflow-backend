package com.touchdown.perflowbackend.file.query.dto;

import com.touchdown.perflowbackend.file.command.domain.aggregate.FileType;
import lombok.Data;

@Data
public class FileResponseDTO {

    private Long id;

    private String originName;

    private String fileName;

    private FileType type;

    private Integer size;

    private String url;
}
