package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import com.touchdown.perflowbackend.approval.command.application.dto.ShareDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class WaitingDocDetailResponseDTO {

    private final Long docId;

    private final String title;

    private final String content;

    private final Long templateId;

    private final List<ApproveLineDTO> approveLines;

    private final List<ShareDTO> shares;

}
