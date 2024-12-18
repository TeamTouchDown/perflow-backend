package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class WaitingDocListResponseDTO {

    private final Long docId;

    private final String title;

    private final String createUserName;

    private final String empId;

    private final LocalDateTime createDatetime;
}
