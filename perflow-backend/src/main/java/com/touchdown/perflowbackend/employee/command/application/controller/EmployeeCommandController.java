package com.touchdown.perflowbackend.employee.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeLoginRequestDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.TokenResponseDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeePwdRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.service.EmployeeCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/employees")
public class EmployeeCommandController {

    private final EmployeeCommandService employeeCommandService;

    @PostMapping
    public ResponseEntity<SuccessCode> registerEmployee(
            @RequestBody EmployeeRegisterDTO employeeRegisterDTO) {

        employeeCommandService.registerEmployee(employeeRegisterDTO);

        return ResponseEntity.ok(SuccessCode.SUCCESS);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessCode> loginRequestEmployee(@RequestBody EmployeeLoginRequestDTO employeeLoginRequestDTO) {

        TokenResponseDTO responseDTO = employeeCommandService.loginRequestEmployee(employeeLoginRequestDTO);

        // 헤더 생성
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + responseDTO.getAccessToken());
        headers.add("refreshToken", "Bearer " + responseDTO.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(SuccessCode.LOGIN_SUCCESS);
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

        headers.add("Authorization", "Bearer " + tokenResponseDTO.getAccessToken());
        headers.add("refreshToken", "Bearer " + tokenResponseDTO.getRefreshToken());

        return ResponseEntity.ok().headers(headers).body(SuccessCode.TOKEN_REISSUE_SUCCESS);
    }
}
