package com.touchdown.perflowbackend.approval.query.dto;

import com.touchdown.perflowbackend.approval.command.application.dto.ApproveLineDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyApproveLineDetailResponseDTO {

    private final String name;

    private final String description;

    private final List<ApproveLineDTO> approveLines;
}
