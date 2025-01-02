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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class EmployeeCommandController {

    private final EmployeeCommandService employeeCommandService;
    private static final String ACCESS_TOKEN_HEADER = "Authorization";
    private static final String REFRESH_TOKEN_HEADER = "refreshToken";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REDIRECT_URI = "http://localhost:5173/employees/pwd";

    @PostMapping("/hr/employees")
    public ResponseEntity<SuccessCode> createEmployee(
            @RequestBody EmployeeCreateDTO employeeCreateDTO) throws Exception {

        employeeCommandService.createEmployee(employeeCreateDTO);

        return ResponseEntity.ok(SuccessCode.EMP_CREATE_SUCCESS);
    }

    @PostMapping("/hr/employees/list")
    public ResponseEntity<SuccessCode> createEmployeeList(
            @RequestPart(value = "empCSV", required = false) MultipartFile empCSV
    ) throws Exception {

        employeeCommandService.createEmployeeList(empCSV);

        return ResponseEntity.ok(SuccessCode.EMP_CSV_CREATE_SUCCESS);
    }

    @PutMapping("/hr/employees") // 사원 정보 수정
    public ResponseEntity<SuccessCode> updateEmployee(
            @RequestBody EmployeeUpdateRequestDTO employeeUpdateRequestDTO
    ) {

        employeeCommandService.updateEmployee(employeeUpdateRequestDTO);

        return ResponseEntity.ok(SuccessCode.EMP_UPDATE_SUCCESS);
    }

    @PutMapping("/employees") // 내 정보 수정
    public ResponseEntity<SuccessCode> updateMyInfo(
            @RequestBody MyInfoUpdateDTO myInfoUpdateDTO
    ) {

        String empId = EmployeeUtil.getEmpId();

        employeeCommandService.updateMyInfo(empId, myInfoUpdateDTO);

        return ResponseEntity.ok(SuccessCode.MY_INFO_UPDATE_SUCCESS);

    }

    @PutMapping("/hr/employees/status")
    public ResponseEntity<SuccessCode> updateEmployeeStatus(
            @RequestBody EmployeeStatusUpdateDTO employeeStatusUpdateDTO
    ) {

        employeeCommandService.updateEmployeeStatus(employeeStatusUpdateDTO);

        return ResponseEntity.ok(SuccessCode.EMP_UPDATE_SUCCESS);
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

    @PutMapping("/employee/pwd")
    public ResponseEntity<SuccessCode> createEmployeePassword(@RequestBody EmployeePwdCreateDTO employeePwdCreateDTO) {

        log.warn(employeePwdCreateDTO.getEmpId());

        employeePwdCreateDTO.setEmpId(employeePwdCreateDTO.getEmpId());

        employeeCommandService.createEmployeePassword(employeePwdCreateDTO);

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

    @PutMapping("/seal")
    public ResponseEntity<SuccessCode> uploadEmployeeSeal(
            @RequestPart(required = false, value = "seal") MultipartFile seal
    ) {

        String empId = EmployeeUtil.getEmpId();

        employeeCommandService.uploadEmployeeSeal(empId, seal);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @GetMapping("/pwdRequest/{token}")
    public ResponseEntity<Object> redirectToPwdRegist(
            @PathVariable(name = "token") String token
    ) throws URISyntaxException {
        employeeCommandService.redirectToPwdRegist(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(REDIRECT_URI+"?token="+token));

        return new ResponseEntity<>(headers, HttpStatus.PERMANENT_REDIRECT);
    }
}
