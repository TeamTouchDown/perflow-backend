package com.touchdown.perflowbackend.approval.query.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class MyApproveLineGroupResponseDTO {

    private final Long groupId;

    private final String name;  // 나의 결재선 이름

    private final String description;   // 나의 결재선 설명

    private final LocalDateTime createDatetime; // 나의 결재선 생성일시
}
