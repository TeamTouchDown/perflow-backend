package com.touchdown.perflowbackend.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 500 에러
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에러 발생"),
    SEND_EMAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패 했습니다."),
    FAIL_READ_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 읽기에 실패 했습니다."),

    // 토큰 관련 에러
    NOT_VALID_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "유효하지 않은 refresh token입니다."),
    NOT_VALID_ACCESS_TOKEN(HttpStatus.FORBIDDEN, "유효하지 않은 access token입니다."),
    SECURITY_CONTEXT_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Security Context에 인증 정보가 없습니다."),
    MISSING_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST, "Authorization 헤더가 누락되었습니다."),

    // 400 에러
    NOT_MATCH_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않은 확장자입니다."),
    INVALID_FIELD_DATA(HttpStatus.BAD_REQUEST, "잘못된 필드 데이터입니다."),
    EMPTY_FIELD_TYPE_DATA(HttpStatus.BAD_REQUEST, "빈 필드 타입 데이터입니다."),
    MISMATCH_FIELD_TYPE_DATA_SIZE(HttpStatus.BAD_REQUEST, "필드 타입 데이터의 크기가 맞지 않습니다."),
    ALREADY_REGISTERED_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호 등록 이력이 존재합니다."),
    ALREADY_REGISTER_COMPANY(HttpStatus.BAD_REQUEST, "회사 데이터는 이미 등록 되어있습니다. 2개 이상의 데이터는 등록 할 수 없습니다."),
    EXCEL_TEMPLATE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, "엑셀 템플릿 업로드 중 오류가 발생했습니다"),
    ALREADY_DELETED_TEMPLATE(HttpStatus.BAD_REQUEST, "이미 삭제된 서식입니다."),
    NOT_MATCHED_CSV(HttpStatus.BAD_REQUEST, "CSV파일 로드에 실패했습니다."),
    NOT_ENOUGH_ANNUAL(HttpStatus.BAD_REQUEST, "연차는 12일 이상의 데이터를 입력해야 합니다."),
    NOT_MATCHED_PAYMENT_DATE(HttpStatus.BAD_REQUEST, "급여 지급일은 1일에서 28일 사이의 데이터를 등록해야합니다."),

    // 401 에러
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 실패"),

    // 403 에러
    AUTHORIZATION_FAILED(HttpStatus.FORBIDDEN, "인가 실패"),
    NOT_MATCH_WRITER(HttpStatus.FORBIDDEN, "작성자가 일치하지 않습니다."),
    NOT_MATCH_DEPARTMENT(HttpStatus.FORBIDDEN, "부서가 일치하지 않습니다."),

    // 404 에러
    NOT_FOUND_EMP(HttpStatus.NOT_FOUND, "사원 정보를 찾을 수 없습니다."),
    NOT_FOUND_COMPANY(HttpStatus.NOT_FOUND, "회사 정보를 찾을 수 없습니다."),
    NOT_FOUND_EMPLOYEE(HttpStatus.NOT_FOUND, "직원 정보를 찾을 수 없습니다."),
    NOT_FOUND_POSITION(HttpStatus.NOT_FOUND, "직위 정보를 찾을 수 없습니다."),
    NOT_FOUND_JOB(HttpStatus.NOT_FOUND, "직책 정보를 찾을 수 없습니다."),
    NOT_FOUND_TEMPLATE(HttpStatus.NOT_FOUND, "서식 정보를 찾을 수 없습니다."),
    NOT_FOUND_FIELD_TYPE(HttpStatus.NOT_FOUND, "필드 타입 정보를 찾을 수 없습니다."),
    NOT_FOUND_DEPARTMENT(HttpStatus.NOT_FOUND, "부서 정보를 찾을 수 없습니다."),
    NOT_FOUND_KPI(HttpStatus.NOT_FOUND,"KPI 정보를 찾을 수 없습니다."),

    NOT_FOUND_APPROVE_SBJ(HttpStatus.NOT_FOUND, "결재 주체 정보를 찾을 수 없습니다."),
    INVALID_APPROVE_SBJ_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 결재 주체 ID입니다."),

    NOT_FOUND_ANNOUNCEMENT(HttpStatus.NOT_FOUND, "해당 공지사항을 찾을 수 없습니다."),

    NOT_FOUND_PAYROLL(HttpStatus.NOT_FOUND, "해당 급여대장을 찾을 수 없습니다."),
    NOT_FOUND_TRAVEL(HttpStatus.NOT_FOUND, "출장 내역을 찾을 수 없습니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST,"승인 반려에 따른 APPROVED, REJECTED 둘중에 하나로 입력 바랍니다." ),
    NOT_FOUND_OVERTIME(HttpStatus.NOT_FOUND, "초과 근무 정보를 찾을 수 없습니다."),
    INVALID_OVERTIME_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 초과 근무 상태입니다. (APPROVED 또는 REJECTED만 작성하세요)");

    private final HttpStatus httpStatus;
    private final String message;
}
