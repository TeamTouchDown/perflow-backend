package com.touchdown.perflowbackend.approval.command.application.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
public class DocCreateRequestDTO {

    private final String title;

    private final String content;

    private final Long templateId;

    private final List<ApproveLineDTO> approveLines;

    // todo: 나중에 공유선도 추가
}
