package com.touchdown.perflowbackend.employee.command.application.controller;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.employee.command.application.dto.*;
import com.touchdown.perflowbackend.employee.command.application.service.EmployeeCommandService;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/employees")
public class EmployeeCommandController {

    private final EmployeeCommandService employeeCommandService;
    private static final String ACCESS_TOKEN_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_HEADER = "refreshToken";
    private static final String BEARER_PREFIX = "Bearer ";

    @PostMapping
    public ResponseEntity<SuccessCode> registerEmployee(
            @RequestBody EmployeeRegisterDTO employeeRegisterDTO) {

        employeeCommandService.registerEmployee(employeeRegisterDTO);

        return ResponseEntity.ok(SuccessCode.EMP_REGISTER_SUCCESS);
    }

    @PostMapping("/list")
    public ResponseEntity<SuccessCode> registerEmployeeList(
            @RequestPart(value = "empCSV", required = false) MultipartFile empCSV
    ) {

        employeeCommandService.registerEmployeeList(empCSV);

        return ResponseEntity.ok(SuccessCode.EMP_CSV_REGISTER_SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessCode> loginRequestEmployee(@RequestBody EmployeeLoginRequestDTO employeeLoginRequestDTO) {

        TokenResponseDTO responseDTO = employeeCommandService.loginRequestEmployee(employeeLoginRequestDTO);

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();

        headers.add(ACCESS_TOKEN_HEADER, BEARER_PREFIX + responseDTO.getAccessToken());
        headers.add(REFRESH_TOKEN_HEADER, BEARER_PREFIX + responseDTO.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(SuccessCode.LOGIN_SUCCESS);
    }

    @PostMapping("/logout")
    public ResponseEntity<SuccessCode> logoutRequestEmployee(HttpServletRequest request) {

        String empId = EmployeeUtil.getEmpId();
        String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (accessToken == null || !accessToken.startsWith(BEARER_PREFIX)) {
            throw new CustomException(ErrorCode.MISSING_AUTHORIZATION_HEADER);
        }

        // Remove "Bearer " prefix
        accessToken = accessToken.substring(7);

        employeeCommandService.logoutRequestEmployee(new EmployeeLogoutRequestDTO(empId, accessToken));

        return ResponseEntity.ok(SuccessCode.LOGOUT_SUCCESS);
    }

    @PutMapping("/pwd")
    public ResponseEntity<SuccessCode> registerEmployeePassword(@RequestBody EmployeePwdRegisterDTO employeePwdRegisterDTO) {

        employeeCommandService.registerEmployeePassword(employeePwdRegisterDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @PostMapping("/reissue")
    public ResponseEntity<SuccessCode> validRefreshToken(
            @RequestHeader(name = "refreshToken") String refreshToken
    ) {

        TokenResponseDTO tokenResponseDTO = employeeCommandService.reissueToken(refreshToken);

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();

        headers.add(ACCESS_TOKEN_HEADER, BEARER_PREFIX + tokenResponseDTO.getAccessToken());
        headers.add(REFRESH_TOKEN_HEADER, BEARER_PREFIX + tokenResponseDTO.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(SuccessCode.TOKEN_REISSUE_SUCCESS);
    }

}
