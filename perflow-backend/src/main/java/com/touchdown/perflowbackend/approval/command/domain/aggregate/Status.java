package com.touchdown.perflowbackend.approval.command.domain.aggregate;

public enum Status {

    ACTIVATED,
    DELETED,
    UPDATED,
    COLLECTED,  // 회수
    DRAFTED,    // 임시 저장
    PENDING,    // 서식 생성 중, 결재주체가 결재하기를 대기 중
    CONFIRMED,  // 결재주체가 승인함
    REJECTED,   // 결재주체가 반려함, 문서가 반려됨
    APPROVED,   // 문서가 승인됨
}
