package com.touchdown.perflowbackend.approval.command.domain.aggregate;

public enum ApproveType {

    SEQ,    // 동의
    CC, // 참조
    AGR,    // 합의
    PLL,    // 병렬
    PLLAGR, // 병렬합의
    ARB // 전결
}
