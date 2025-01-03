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
    EMP_UPDATE_SUCCESS(HttpStatus.OK, "사원 정보 수정 성공"),
    DEPARTMENT_UPDATE_SUCCESS(HttpStatus.OK, "부서 정보 수정 성공"),
    POSITION_UPDATE_SUCCESS(HttpStatus.OK,"직위 정보 수정 성공"),
    POSITION_DELETE_SUCCESS(HttpStatus.OK,"직위 정보 삭제 성공"),
    JOB_UPDATE_SUCCESS(HttpStatus.OK,"직책 정보 수정 성공"),
    JOB_DELETE_SUCCESS(HttpStatus.OK,"직책 정보 삭제 성공"),
    MY_INFO_UPDATE_SUCCESS(HttpStatus.OK, "내 정보 수정 성공"),
    PASSWORD_CREATE_SUCCESS(HttpStatus.OK, "초기 비밀번호 등록 성공"),
    TOKEN_REISSUE_SUCCESS(HttpStatus.OK, "AccessToken 재발급 성공!"),

    // 200 - 회사 관련 성공
    COMPANY_UPDATE_SUCCESS(HttpStatus.OK, "회사 정보 수정 성공"),
    COMPANY_DELETE_SUCCESS(HttpStatus.OK, "회사 정보 삭제 성공"),

    // 200 - 급여 관련 성공
    EXCEL_TEMPLATE_UPLOAD_SUCCESS(HttpStatus.OK, "엑셀 템플릿을 성공적으로 업로드 하였습니다."),
    PAYROLL_UPLOAD_SUCCESS(HttpStatus.OK, "급여대장을 성공적으로 업로드 하였습니다."),
    SEVERANCE_PAY_UPLOAD_SUCCESS(HttpStatus.OK, "퇴직금 정보를 성공적으로 업로드 하였습니다."),
    PAYROLL_ADJUSTMENT_SUCCESS(HttpStatus.OK, "급여 정산을 완료하였습니다."),
    SEVERANCE_PAY_ADJUSTMENT_SUCCESS(HttpStatus.OK, "퇴직금 정산을 완료하였습니다."),
    INSURANCE_RATE_SETTING_SUCCESS(HttpStatus.OK, "보험료율 설정을 성공하였습니다."),
    INSURANCE_RATE_UPDATE_SUCCESS(HttpStatus.OK, "보험료율 수정을 성공하였습니다."),

    // 200 결재 관련 성공
    TEMPLATE_CREATE_SUCCESS(HttpStatus.CREATED, "서식 생성에 성공하였습니다"),
    TEMPLATE_UPDATE_SUCCESS(HttpStatus.OK, "서식 수정에 성공하였습니다."),
    TEMPLATE_DELETE_SUCCESS(HttpStatus.OK, "서식 삭제에 성공하였습니다."),
    DOC_CREATE_SUCCESS(HttpStatus.CREATED, "문서 생성에 성공하였습니다."),
    DOC_APPROVE_SUCCESS(HttpStatus.OK, "문서 결재에 성공하였습니다."),
    MY_APPROVE_LINE_CREATE_SUCCESS(HttpStatus.CREATED, "나의 결재선 생성에 성공하였습니다."),
    MY_APPROVE_LINE_UPDATE_SUCCESS(HttpStatus.OK, "나의 결재선 수정에 성공하였습니다."),
    MY_APPROVE_LINE_DELETE_SUCCESS(HttpStatus.OK, "나의 결재선 삭제에 성공하였습니다."),

    // 200 - 인사 평가 관련 성공

    // 개인 KPI
    KPI_PERSONAL_UPLOAD_SUCCESS(HttpStatus.OK, "개인 KPI를 성공적으로 등록하였습니다."),
    KPI_PERSONAL_UPDATE_SUCCESS(HttpStatus.OK, "개인 KPI를 성공적으로 수정하였습니다."),
    KPI_PERSONAL_DELETE_SUCCESS(HttpStatus.OK, "개인 KPI를 성공적으로 삭제하였습니다."),

    // 팀 KPI
    KPI_TEAM_UPLOAD_SUCCESS(HttpStatus.OK, "팀 KPI를 성공적으로 등록하였습니다."),
    KPI_TEAM_UPDATE_SUCCESS(HttpStatus.OK, "팀 KPI를 성공적으로 수정하였습니다."),
    KPI_TEAM_DELETE_SUCCESS(HttpStatus.OK, "팀 KPI를 성공적으로 삭제하였습니다."),

    // KPI 최신화
    KPI_PROGRESS_UPLOAD_SUCCESS(HttpStatus.OK,"KPI를 성공적으로 최신화하였습니다."),

    // KPI 처리
    KPI_PASS_UPLOAD_SUCCESS(HttpStatus.OK,"KPI를 성공적으로 처리하였습니다."),

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

    // 평가 조정
    EVALUTION_ADJUSTMENT_UPDATE_SUCCESS(HttpStatus.OK,"평가 조정이 성공적으로 저장되었습니다."),

    // 인사 평가
    HRPERFO_UPLOAD_SUCCESS(HttpStatus.OK, "인사 평가가 성공적으로 작성되었습니다."),
    HRPERFO_UPDATE_SUCCESS(HttpStatus.OK, "인사 평가가 성공적으로 수정되었습니다."),

    // 인사 평가 의의 제기
    HRPERFO_INQUIRY_UPLOAD_SUCCESS(HttpStatus.OK, "인사 평가 의의제기가 성공적으로 작성되었습니다."),
    HRPERFO_INQUIRY_UPDATE_SUCCESS(HttpStatus.OK, "인사 평가 의의제기가 성공적으로 수정되었습니다."),

    // 인사 평가 비율
    RATIO_PERFO_UPLOAD_SUCCESS(HttpStatus.OK, "인사 평가 비율이 성공적으로 입력되었습니다."),

    // 등급 비율
    RATIO_GRADE_UPLOAD_SUCCESS(HttpStatus.OK, "등급 비율이 성공적으로 입력되었습니다."),
    RATIO_GRADE_UPDATE_SUCCESS(HttpStatus.OK, "등급 비율이 성공적으로 수정되었습니다."),

    // AI 요약
    AI_SUMMARY_UPLOAD_SUCCESS(HttpStatus.OK, "AI 요약이 정상적으로 생성되었습니다."),

    // 201
    WORK_ATTITUDE_TRAVEL_SUCCESS(HttpStatus.CREATED, "출장 요청 등록이 완료 되었습니다."),
    EMP_CREATE_SUCCESS(HttpStatus.CREATED, "사원 등록 성공"),
    EMP_CSV_CREATE_SUCCESS(HttpStatus.CREATED, "사원 목록 등록 성공"),
    COMPANY_CREATE_SUCCESS(HttpStatus.CREATED, "회사 정보 등록 성공"),
    DEPARTMENT_CREATE_SUCCESS(HttpStatus.CREATED, "부서 정보 등록 성공"),
    POSITION_CREATE_SUCCESS(HttpStatus.CREATED, "직위 정보 등록 성공"),
    JOB_CREATE_SUCCESS(HttpStatus.CREATED, "직책 정보 등록 성공"),
    APPOINT_CREATE_SUCCESS(HttpStatus.CREATED, "발령 정보 등록 성공"),

    //초과근무
    WORK_ATTRIBUTE_OVERTIME_SUCCESS(HttpStatus.OK, "초과근무 등록이 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_UPDATE_SUCCESS(HttpStatus.OK, "초과근무 수정이 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_DELETE_SUCCESS(HttpStatus.OK, "초과근무 삭제가 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_DELETE_FOR_LEADER_SUCCESS(HttpStatus.OK, "상태 변경이 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_RETROACTIVE_SUCCESS(HttpStatus.OK, "소급 신청이 완료되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_RETROACTIVE_DECISION_SUCCESS(HttpStatus.OK, "소급 승인/반려 처리가 완료되었습니다."),

    // 출퇴근
    WORK_ATTITUDE_ATTENDANCE_CHECK_IN_SUCCESS(HttpStatus.OK,"출근 처리가 완료 되었습니다." ),
    WORK_ATTITUDE_ATTENDANCE_CHECK_OUT_SUCCESS(HttpStatus.OK,"퇴근 처리가 완료 되었습니다." ),


    QR_CODE_VALIDATION_SUCCESS(HttpStatus.OK,"인증이 완료 되었습니다." ),
    WORK_ATTITUDE_ANNUAL_SUCCESS(HttpStatus.OK, "연차 신청 완료되었습니다."),
    WORK_ATTRIBUTE_ANNUAL_UPDATE_SUCCESS(HttpStatus.OK,"연차 수정 완료 되었습니다." ),
    WORK_ATTRIBUTE_ANNUAL_DELETE_SUCCESS(HttpStatus.OK,"연차 삭제 완료 되었습니다." ),
    WORK_ATTITUDE_ANNUAL_CHECK_IN_SUCCESS(HttpStatus.OK, "승인 처리 완료 되었습니다."),
    WORK_ATTITUDE_ANNUAL_CHECK_OUT_SUCCESS(HttpStatus.OK,"작성 완료 되었습니다." ),
    WORK_ATTITUDE_VACATION_CHECK_OUT_SUCCESS(HttpStatus.OK, "처리 완료 되었습니다."),
    WORK_ATTITUDE_VACATION_CHECK_IN_SUCCESS(HttpStatus.OK,"승인 처리 완료 되었습니다."),
    WORK_ATTRIBUTE_VACATION_DELETE_SUCCESS(HttpStatus.OK,"삭제 처리 완료 되었습니다."),
    WORK_ATTRIBUTE_VACATION_UPDATE_SUCCESS(HttpStatus.OK,"업데이트 완료 되었습니다."),
    WORK_ATTITUDE_VACATION_SUCCESS(HttpStatus.OK,"휴가 신청 완료 되었습니다."),
    WORK_ATTRIBUTE_OVERTIME_APPROVED(HttpStatus.OK,"초과근무 승인이 완료 되었습니다." ),
    WORK_ATTRIBUTE_OVERTIME_REJECTED(HttpStatus.OK, "초과근무 반려가 완료 되었습니다."),
    WORK_ATTRIBUTE_VACATION_SUCCESS(HttpStatus.OK,"휴가 신청이 완료되었습니다." );






    private final HttpStatus httpStatus;
    private final String message;
}
