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
    INVALID_FIELD_DATA(HttpStatus.BAD_REQUEST, "잘못된 필드 데이터입니다."),
    ALREADY_REGISTERED_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 등록 이력이 존재합니다."),
    EXCEL_TEMPLATE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "엑셀 템플릿 업로드 중 오류가 발생했습니다"),

    // 401 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 실패"),

    // 403 에러
    AUTHORIZATION_FAILED(HttpStatus.FORBIDDEN, "인가 실패"),
    NOT_MATCH_WRITER(HttpStatus.FORBIDDEN, "작성자가 일치하지 않습니다."),
    NOT_MATCH_DEPARTMENT(HttpStatus.FORBIDDEN, "부서가 일치하지 않습니다."),

    // 404 에러
    NOT_FOUND_EMP(HttpStatus.NOT_FOUND, "사원 정보를 찾을 수 없습니다."),
    NOT_FOUND_EMPLOYEE(HttpStatus.NOT_FOUND, "직원 정보를 찾을 수 없습니다."),
    NOT_FOUND_POSITION(HttpStatus.NOT_FOUND, "직위 정보를 찾을 수 없습니다."),
    NOT_FOUND_JOB(HttpStatus.NOT_FOUND, "직책 정보를 찾을 수 없습니다."),
    NOT_FOUND_TEMPLATE(HttpStatus.NOT_FOUND, "서식 정보를 찾을 수 없습니다."),
    NOT_FOUND_FIELD_TYPE(HttpStatus.NOT_FOUND, "필드 유형 정보를 찾을 수 없습니다."),
    NOT_FOUND_DEPARTMENT(HttpStatus.NOT_FOUND, "부서 정보를 찾을 수 없습니다."),
    NOT_FOUND_KPI(HttpStatus.NOT_FOUND,"KPI 정보를 찾을 수 없습니다."),

    NOT_FOUND_APPROVE_SBJ(HttpStatus.NOT_FOUND, "결재 주체 정보를 찾을 수 없습니다."),
    INVALID_APPROVE_SBJ_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 결재 주체 ID입니다."),

    NOT_FOUND_ANNOUNCEMENT(HttpStatus.NOT_FOUND, "해당 공지사항을 찾을 수 없습니다."),

    NOT_FOUND_PAYROLL(HttpStatus.NOT_FOUND, "해당 급여대장을 찾을 수 없습니다."),
    NOT_FOUND_TRAVEL(HttpStatus.NOT_FOUND, "출장 내역을 찾을 수 없습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
