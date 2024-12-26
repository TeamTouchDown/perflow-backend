package com.touchdown.perflowbackend.approval.query.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class InboxDocDetailResponseDTO {

    private final Long docId; // 문서 ID

    private final String createUserName;

    private final String createUserDept;

    private final String createUserPosition;

    private final LocalDateTime createDatetime;

    private final String title; // 문서 제목

    private final Map<String, Object> fields; // 필드 데이터 (키 - 값)

    private final List<InboxDocApproveLineDTO> approveLines; // 결재선 정보

    private final List<InboxDocShareDTO> shares; // 공유 설정 정보

    private final String myStatus; // 내 결재 상태

    private final String docStatus; // 문서 전체 상태

}
