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
    LOGOUT_SUCCESS(HttpStatus.OK, "로그아웃 성공"),
    EMP_REGISTER_SUCCESS(HttpStatus.OK, "사원 등록 성공"),
    EMP_CSV_REGISTER_SUCCESS(HttpStatus.OK, "사원 목록 등록 성공"),
    PASSWORD_REGISTER_SUCCESS(HttpStatus.OK, "초기 비밀번호 등록 성공"),
    TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "AccessToken 재발급 성공!"),

    // 200 - 급여대장 관련 성공
    EXCEL_TEMPLATE_UPLOAD_SUCCESS(HttpStatus.OK, "엑셀 템플릿을 성공적으로 업로드 하였습니다."),
    PAYROLL_UPLOAD_SUCCESS(HttpStatus.OK, "급여대장을 성공적으로 업로드 하였습니다."),

    // 200 결재 관련 성공
    TEMPLATE_CREATE_SUCCESS(HttpStatus.OK, "서식 생성에 성공하였습니다"),
    TEMPLATE_UPDATE_SUCCESS(HttpStatus.OK, "서식 수정에 성공하였습니다."),
    TEMPLATE_DELETE_SUCCESS(HttpStatus.OK, "서식 삭제에 성공하였습니다."),

    // 200 - 인사 평가 관련 성공

    // 개인 KPI
    KPI_PERSONAL_UPLOAD_SUCCESS(HttpStatus.OK, "개인 KPI를 성공적으로 등록하였습니다."),
    KPI_PERSONAL_UPDATE_SUCCESS(HttpStatus.OK, "개인 KPI를 성공적으로 수정하였습니다."),
    KPI_PERSONAL_DELETE_SUCCESS(HttpStatus.OK, "개인 KPI를 성공적으로 삭제하였습니다."),

    // 팀 KPI
    KPI_TEAM_UPLOAD_SUCCESS(HttpStatus.OK, "팀 KPI를 성공적으로 등록하였습니다."),
    KPI_TEAM_UPDATE_SUCCESS(HttpStatus.OK, "팀 KPI를 성공적으로 수정하였습니다."),
    KPI_TEAM_DELETE_SUCCESS(HttpStatus.OK, "팀 KPI를 성공적으로 삭제하였습니다."),

    // 동료 평가
    EVALUTION_COL_UPLOAD_SUCCESS(HttpStatus.OK, "동료 평가가 성공적으로 작성되었습니다."),
    EVALUTION_COL_UPDATE_SUCCESS(HttpStatus.OK, "동료 평가가 성공적으로 수정되었습니다."),

    // 동료 평가 문항 생성
    EVALUTION_COL_QUESTION_UPLOAD_SUCCESS(HttpStatus.OK, "동료 평가 문항이 성공적으로 생성되었습니다."),
    EVALUTION_COL_QUESTION_UPDATE_SUCCESS(HttpStatus.OK, "동료 평가 문항이 성공적으로 수정되었습니다."),
    EVALUTION_COL_QUESTION_DELETE_SUCCESS(HttpStatus.OK, "동료 평가 문항이 성공적으로 삭제되었습니다."),

    // 하향 평가
    EVALUTION_DOWN_UPLOAD_SUCCESS(HttpStatus.OK, "하향 평가가 성공적으로 작성되었습니다."),
    EVALUTION_DOWN_UPDATE_SUCCESS(HttpStatus.OK, "하향 평가가 성공적으로 수정되었습니다."),

    // 동료 평가 문항 생성
    EVALUTION_DOWN_QUESTION_UPLOAD_SUCCESS(HttpStatus.OK, "하향 평가 문항이 성공적으로 생성되었습니다."),
    EVALUTION_DOWN_QUESTION_UPDATE_SUCCESS(HttpStatus.OK, "하향 평가 문항이 성공적으로 수정되었습니다."),
    EVALUTION_DOWN_QUESTION_DELETE_SUCCESS(HttpStatus.OK, "하향 평가 문항이 성공적으로 삭제되었습니다."),

    // 인사 평가
    HRPERFO_UPLOAD_SUCCESS(HttpStatus.OK, "인사 평가가 성공적으로 작성되었습니다."),
    HRPERFO_UPDATE_SUCCESS(HttpStatus.OK, "인사 평가가 성공적으로 수정되었습니다."),

    // 201
    WORK_ATTITUDE_TRAVEL_SUCCESS(HttpStatus.CREATED, "출장 요청 등록이 완료 되었습니다."),

    //초과근무
    WORK_ATTRIBUTE_OVERTIME_SUCCESS(HttpStatus.OK, "초과근무 등록이 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_UPDATE_SUCCESS(HttpStatus.OK, "초과근무 수정이 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_DELETE_SUCCESS(HttpStatus.OK, "초과근무 삭제가 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_DELETE_FOR_LEADER_SUCCESS(HttpStatus.OK, "상태 변경이 완료 되었습니다."),


    ;

    private final HttpStatus httpStatus;
    private final String message;
}
