package com.touchdown.perflowbackend.Approval.command.domain.aggregate;

public enum Status {

    ACTIVATED,
    DELETED,
    UPDATED,
    COLLECTED,  // 회수
    DRAFTED,    // 임시 저장
    PENDING,    // 서식 생성 중
    CONFIRMED,  // 승인됨
}
