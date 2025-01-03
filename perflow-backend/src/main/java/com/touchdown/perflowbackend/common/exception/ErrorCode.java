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
    FAIL_CREAT_FILE(HttpStatus.INTERNAL_SERVER_ERROR, "파일 생성에 실패 했습니다."),

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
    NOT_EXIST_UPLOAD_FILE(HttpStatus.BAD_REQUEST, "업로드할 파일이 없습니다."),
    UNSUPPORTED_ENTITY(HttpStatus.BAD_REQUEST, "지원되지 않는 도메인입니다."),
    UNSUPPORTED_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "지원되지 않는 확장자입니다."),
    INVALID_APPROVE_TEMPLATE_TYPE(HttpStatus.BAD_REQUEST, "잘못된 결재선 형식입니다."),
    INVALID_APPROVE_TYPE(HttpStatus.BAD_REQUEST, "잘못된 결재 방식입니다."),
    NOT_MATCHED_CSV(HttpStatus.BAD_REQUEST, "CSV파일 로드에 실패했습니다."),
    NOT_ENOUGH_ANNUAL(HttpStatus.BAD_REQUEST, "잔여 연차가 없습니다."),
    NOT_MATCHED_PAYMENT_DATE(HttpStatus.BAD_REQUEST, "급여 지급일은 1일에서 28일 사이의 데이터를 등록해야합니다."),
    ALREADY_CREATE_PIC(HttpStatus.BAD_REQUEST, "이미 존재하는 담당자 데이터입니다. 부서와 담당자를 확인해주세요."),
    TOO_MANY_PROMOTION_STEPS(HttpStatus.BAD_REQUEST,"승진과 강등은 한번에 한 단계씩만 가능합니다."),
    DUPLICATE_DEPT_REQUEST(HttpStatus.BAD_REQUEST, "이미 소속된 부서입니다. 다른 부서를 선택해주세요."),
    INVALID_ANNUAL_REQUEST(HttpStatus.BAD_REQUEST, "유효하지 않은 연차 요청입니다."),


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
    NOT_FOUND_TEMPLATE_FIELD(HttpStatus.NOT_FOUND, "서식 필드 정보를 찾을 수 없습니다."),
    NOT_FOUND_FIELD_TYPE(HttpStatus.NOT_FOUND, "필드 타입 정보를 찾을 수 없습니다."),
    NOT_FOUND_DEPARTMENT(HttpStatus.NOT_FOUND, "부서 정보를 찾을 수 없습니다."),
    NOT_FOUND_MY_APPROVE_LINE(HttpStatus.NOT_FOUND, "나의 결재선 정보를 찾을 수 없습니다."),
    NOT_FOUND_MANAGED_DEPARTMENT(HttpStatus.NOT_FOUND, "상위 부서 정보를 찾을 수 없습니다."),
    NOT_FOUND_APPROVE_LINE(HttpStatus.NOT_FOUND, "결재선 정보를 찾을 수 없습니다."),
    NOT_FOUND_DOC(HttpStatus.NOT_FOUND, "문서 정보를 찾을 수 없습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    NOT_FOUND_KPI(HttpStatus.NOT_FOUND,"KPI 정보를 찾을 수 없습니다."),
    NOT_FOUND_HRPERFO(HttpStatus.NOT_FOUND,"평가 점수 정보를 찾을 수 없습니다."),
    NOT_FOUND_HRPERFOINQUIRY(HttpStatus.NOT_FOUND,"인사 평가 의의제기 정보를 찾을 수 없습니다."),
    NOT_FOUND_FILE(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    NOT_FOUND_PERFOQUESTION(HttpStatus.NOT_FOUND,"문항 정보를 찾을 수 없습니다."),
    NOT_FOUND_PERFOQUESTION_ANSWER(HttpStatus.NOT_FOUND,"문항 답변을 찾을 수 없습니다."),
    NOT_FOUND_AI_RESPONSE(HttpStatus.NOT_FOUND,"AI로 부터 응답이 없습니다."),

    NOT_FOUND_APPROVE_SBJ(HttpStatus.NOT_FOUND, "결재 주체 정보를 찾을 수 없습니다."),
    INVALID_APPROVE_SBJ_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 결재 주체 ID입니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 날짜 형식입니다."),
    INVALID_MONEY_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 금액 형식입니다."),
    UNSUPPORTED_FIELD_TYPE(HttpStatus.BAD_REQUEST, "지원하지 않는 필드 타입입니다."),
    INVALID_SHARE_TYPE(HttpStatus.BAD_REQUEST, "잘못된 공유 타입입니다."),

    NOT_FOUND_ANNOUNCEMENT(HttpStatus.NOT_FOUND, "해당 공지사항을 찾을 수 없습니다."),

    NOT_FOUND_PAYROLL(HttpStatus.NOT_FOUND, "해당 급여대장을 찾을 수 없습니다."),
    NOT_FOUND_SEVERANCE_PAY(HttpStatus.NOT_FOUND, "해당 퇴직금 정보를 찾을 수 없습니다."),
    NOT_FOUND_INSURANCE_RATE(HttpStatus.NOT_FOUND, "해당 보험료율 설정을 찾을 수 없습니다."),
    NOT_FOUND_TRAVEL(HttpStatus.NOT_FOUND, "출장 내역을 찾을 수 없습니다."),
    INVALID_STATUS(HttpStatus.BAD_REQUEST,"승인 반려에 따른 APPROVED, REJECTED 둘중에 하나로 입력 바랍니다." ),
    NOT_FOUND_OVERTIME(HttpStatus.NOT_FOUND, "초과 근무 정보를 찾을 수 없습니다."),
    INVALID_OVERTIME_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 초과 근무 상태입니다. (APPROVED 또는 REJECTED만 작성하세요)"),
    INVALID_RETROACTIVE_DECISION(HttpStatus.BAD_REQUEST, "유효하지 않은 소급 처리 요청입니다."),
    ALREADY_APPLIED_RETROACTIVE(HttpStatus.BAD_REQUEST, "이미 소급 신청된 초과근무입니다."),
    ALREADY_CHECKED_IN(HttpStatus.BAD_REQUEST,"출근 완료된 상태입니다."),
    NOT_FOUND_ATTENDANCE(HttpStatus.BAD_REQUEST,"출근 처리가 안된 상태입니다."),
    INVALID_QR_CODE(HttpStatus.BAD_REQUEST,"올바르지 않은 인증입니다."),
    NOT_FOUND_TEAM(HttpStatus.BAD_REQUEST,"해당 팀을 찾을 수 없습니다."),
    NOT_FOUND_ATTENDANCE_DATA(HttpStatus.NOT_FOUND, "근무 시간 데이터를 찾을 수 없습니다."),
    NOT_FOUND_AUTH(HttpStatus.NOT_FOUND, "권한을 찾을 수 없습니다." ),

    NOT_FOUND_ANNUAL(HttpStatus.NOT_FOUND, "연차 정보를 찾을 수 없습니다."),

    ACCESS_DENIED(HttpStatus.BAD_REQUEST, "올바르지 않은 접근입니다. "),
    NOT_FOUND_VACATION(HttpStatus.BAD_REQUEST, "휴가를 찾을 수 없습니다."),
    DUPLICATE_VACATION(HttpStatus.BAD_REQUEST, "중복된 일정으로 휴가 신청이 불가합니다."),
    DUPLICATE_JOB_REQUEST(HttpStatus.NOT_FOUND,"직책이 중복됩니다." ),
    INVALID_DATE_RANGE(HttpStatus.BAD_REQUEST, "잘못된 날짜 형식입니다."),
    DUPLICATE_ANNUAL(HttpStatus.BAD_REQUEST, "중복된 일정으로 연차 신청이 불가능합니다."),
    UNAUTHORIZED(HttpStatus.BAD_REQUEST,"결재권자가 아닙니다." ),
    INVALID_OVERTIME_REQUEST(HttpStatus.BAD_REQUEST, "초과근무 신청시간이 아닙니다."),
    NO_AUTHORITY(HttpStatus.NOT_FOUND, " 권한이 없습니다."),
    DUPLICATE_TRAVEL(HttpStatus.BAD_REQUEST, "중복된 출장 신청입니다."),
    DUPLICATE_OVERTIME(HttpStatus.BAD_REQUEST,"이미 신청된 초과근무가 있습니다."),
    ALREADY_CONFIRMED(HttpStatus.BAD_REQUEST,"승인된 상태에서는 수정할 수 없습니다." ),
    INVALID_RETROACTIVE_REASON(HttpStatus.BAD_REQUEST ,"올바르지 않은 요청입니다.");

;


    private final HttpStatus httpStatus;
    private final String message;
}
