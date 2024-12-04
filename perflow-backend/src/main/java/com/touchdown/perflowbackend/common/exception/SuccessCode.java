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

    // 201
    WORK_ATTITUDE_TRAVEL_SUCCESS(HttpStatus.CREATED, "출장 요청 등록이 완료 되었습니다."),

    TEMPLATE_CREATE_SUCCESS(HttpStatus.OK, "서식 생성에 성공하였습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
