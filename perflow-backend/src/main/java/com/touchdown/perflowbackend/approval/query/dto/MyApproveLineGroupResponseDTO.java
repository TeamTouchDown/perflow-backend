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

    private final List<MyApproveLineResponseDTO> lines;   // 나의 결재선 그룹에 속한 결재 요소들

    public MyApproveLineGroupResponseDTO(Long groupId, String name, String description, LocalDateTime createDatetime) {
        this.groupId = groupId;
        this.name = name;
        this.description = description;
        this.createDatetime = createDatetime;
        this.lines = null; // JPQL 처리 이후 별도 설정
    }
}
