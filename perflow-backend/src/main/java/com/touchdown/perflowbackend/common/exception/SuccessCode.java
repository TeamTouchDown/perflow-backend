package com.touchdown.perflowbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum SuccessCode {

    //SUCCESS(상태코드, "성공 문구");

    // 200
    SUCCESS(HttpStatus.OK, "OK"),

    // 200 - 인사정보 관련 성공
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    PASSWORD_REGISTER_SUCCESS(HttpStatus.OK, "초기 비밀번호 등록 성공")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
