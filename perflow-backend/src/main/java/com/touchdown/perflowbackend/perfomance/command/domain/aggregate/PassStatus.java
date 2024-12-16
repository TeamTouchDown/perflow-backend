package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

public enum PassStatus {

    // 처리 상태
    WAIT, // 대기
    REJECT, // 반려
    PASS // 처리
}
