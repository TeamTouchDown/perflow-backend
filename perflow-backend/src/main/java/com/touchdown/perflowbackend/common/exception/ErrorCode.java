package com.touchdown.perflowbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 500 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 발생"),

    // 토큰 관련 에러

    // 400 에러
    NOT_MATCH_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않은 확장자입니다."),

    // 401 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 실패"),

    // 403 에러
    AUTHORIZATION_FAILED(HttpStatus.FORBIDDEN, "인가 실패"),

    // 404 에러
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사원 정보를 찾을 수 없습니다."),

    NOT_FOUND_TEMPLATE(HttpStatus.NOT_FOUND, "서식 정보를 찾을 수 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
