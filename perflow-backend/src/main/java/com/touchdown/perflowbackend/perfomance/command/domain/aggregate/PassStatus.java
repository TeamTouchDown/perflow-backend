package com.touchdown.perflowbackend.perfomance.command.domain.aggregate;

public enum PassStatus {

    // 처리 상태
    REJECT, // 반려 상태
    APPROVAL, // 승인(승인 처리 되었으나 아직 KPI 활성화 전)
    ACTIVE, // 활성화(승인 처리 된 후 KPI 현황을 업데이트 가능한 상태)
    EXPIRED // 만료(이미 완료된 KPI)
}
