package com.touchdown.perflowbackend.approval.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DocCreateRequestDTO {

    private final String title;

    private final String content;

    private final Long templateId;

    private final List<ApproveLineDTO> approveLines;

    private final List<ShareDTO> shares;
}
