package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class OutboxDocDetailResponseDTO {

    private final Long docId;

    private final String createUserName;

    private final String createUserDept;

    private final String createUserPosition;

    private final LocalDateTime createDatetime;

    private final String title;

    private final Map<String, Object> fields;

    private final List<OutboxDocApproveLineDTO> approveLines;

    private final List<OutboxDocShareDTO> shares;

    private final String status;
}
