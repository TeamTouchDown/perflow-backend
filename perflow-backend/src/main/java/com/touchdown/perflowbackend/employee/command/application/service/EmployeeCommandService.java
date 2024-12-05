package com.touchdown.perflowbackend.employee.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeLoginRequestDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.TokenResponseDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeePwdRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeRegisterDTO;
import com.touchdown.perflowbackend.employee.command.application.mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import com.touchdown.perflowbackend.security.filter.JwtFilter;
import com.touchdown.perflowbackend.security.redis.BlackAccessToken;
import com.touchdown.perflowbackend.security.redis.WhiteRefreshToken;
import com.touchdown.perflowbackend.security.repository.BlackAccessTokenRepository;
import com.touchdown.perflowbackend.security.repository.WhiteRefreshTokenRepository;
import com.touchdown.perflowbackend.security.util.CustomEmployDetail;
import com.touchdown.perflowbackend.security.util.JwtTokenProvider;
import com.touchdown.perflowbackend.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeCommandService {

    private final EmployeeCommandRepository employeeCommandRepository;
    private final PositionCommandRepository positionCommandRepository;
    private final JobCommandRepository jobCommandRepository;
    private final DepartmentCommandRepository departmentCommandRepository;
    private final WhiteRefreshTokenRepository whiteRefreshTokenRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerEmployee(EmployeeRegisterDTO employeeRegisterDTO) {

        Department department = departmentCommandRepository.findById(employeeRegisterDTO.getDepartmentId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT)
        );

        Position position = positionCommandRepository.findById(employeeRegisterDTO.getPositionId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_POSITION)
        );
        Job job = jobCommandRepository.findById(employeeRegisterDTO.getJobId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_JOB)
        );

        Employee newEmployee = EmployeeMapper.toEntity(employeeRegisterDTO, position, job, department);

        employeeCommandRepository.save(newEmployee);

    }


    @Transactional
    public TokenResponseDTO loginRequestEmployee(EmployeeLoginRequestDTO employeeLoginRequestDTO) {

        // 1. AuthenticationManager를 통해 인증 수행
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        employeeLoginRequestDTO.getEmpId(),
                        employeeLoginRequestDTO.getPassword()
                )
        );

        CustomEmployDetail customEmployDetail = (CustomEmployDetail) authentication.getPrincipal();

        String empId = customEmployDetail.getUsername();
        EmployeeStatus status = customEmployDetail.getStatus();

        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", empId);
        claims.put("status", status.name());

        String accessToken = jwtTokenProvider.createAccessToken(customEmployDetail.getUsername(), claims);
        String refreshToken = jwtTokenProvider.createRefreshToken(customEmployDetail.getUsername(), claims);

        /* refreshToken 화이트 리스트로 redis에 저장 */
        whiteRefreshTokenRepository.save(new WhiteRefreshToken(refreshToken, empId));

        return new TokenResponseDTO(empId, accessToken, refreshToken);
    }

    @Transactional
    public void registerEmployeePassword(EmployeePwdRegisterDTO employeePwdRegisterDTO) {

        Employee employee = employeeCommandRepository.findById(employeePwdRegisterDTO.getEmpId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );

        /* 이미 초기 비밀번호 등록이 완료된 사원이라면 등록 불가. */
        if (!employee.getPassword().isEmpty()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_PASSWORD);
        }

        log.info(employee.toString());
        String pwd = passwordEncoder.encode(employeePwdRegisterDTO.getPassword());

        employee.registerPassword(pwd);

        employeeCommandRepository.save(employee);
    }

    public TokenResponseDTO validRefreshToken(String refreshToken) {

        /* token의 기본 유효성 확인 */
        if(!jwtUtil.validateToken(refreshToken)) { // refreshToken이 유효하지 않을 경우.
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }

        String empId = jwtUtil.getUserId(refreshToken);
        Map<String, Object> claims = jwtUtil.parseClaims(refreshToken);

        /* whiteList 에 포함된 refreshToken 인지 확인 */
        whiteRefreshTokenRepository.findById(refreshToken).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN)
        );

        String newAccessToken = jwtTokenProvider.createAccessToken(empId, claims);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(empId, claims);

        /* refreshToken 화이트 리스트로 redis에 저장 */
        whiteRefreshTokenRepository.save(new WhiteRefreshToken(refreshToken, empId));

        return new TokenResponseDTO(empId, newAccessToken, newRefreshToken);
    }
}
