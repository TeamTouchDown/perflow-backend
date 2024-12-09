package com.touchdown.perflowbackend.employee.command.application.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.*;
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
import com.touchdown.perflowbackend.security.redis.BlackAccessToken;
import com.touchdown.perflowbackend.security.redis.WhiteRefreshToken;
import com.touchdown.perflowbackend.security.repository.BlackAccessTokenRepository;
import com.touchdown.perflowbackend.security.repository.WhiteRefreshTokenRepository;
import com.touchdown.perflowbackend.security.util.CustomEmployDetail;
import com.touchdown.perflowbackend.security.util.JwtTokenProvider;
import com.touchdown.perflowbackend.security.util.JwtUtil;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final BlackAccessTokenRepository blackAccessTokenRepository;

    private final AuthenticationManager authenticationManager;
    private final EntityManager entityManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void registerEmployee(EmployeeRegisterDTO employeeRegisterDTO) {

        Department department = departmentCommandRepository.findById(employeeRegisterDTO.getDepartmentId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
        Position position = positionCommandRepository.findById(employeeRegisterDTO.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));
        Job job = jobCommandRepository.findById(employeeRegisterDTO.getJobId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_JOB));

        Employee newEmployee = EmployeeMapper.toEntity(employeeRegisterDTO, position, job, department);

        entityManager.persist(newEmployee);
        employeeCommandRepository.save(newEmployee);
        entityManager.flush();

        sendInvitationEmail(newEmployee);
    }

    @Transactional
    public void registerEmployeeList(MultipartFile empCSV) {

        String originalFilename = empCSV.getOriginalFilename();
        String extension = FileNameUtils.getExtension(originalFilename);

        if (!isCSV(extension)) {
            throw new CustomException(ErrorCode.NOT_MATCHED_CSV);
        } else {
            List<Employee> empList = getEmpList(empCSV);

            for (Employee emp : empList) {
                entityManager.persist(emp);
                employeeCommandRepository.save(emp);
                entityManager.flush();

                sendInvitationEmail(emp);
            }
        }
    }

    @Transactional
    public TokenResponseDTO loginRequestEmployee(EmployeeLoginRequestDTO employeeLoginRequestDTO) {

        // 1. AuthenticationManager를 통해 인증 수행
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(employeeLoginRequestDTO.getEmpId(), employeeLoginRequestDTO.getPassword()));

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

        Employee employee = employeeCommandRepository.findById(employeePwdRegisterDTO.getEmpId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_EMP));

        /* 이미 초기 비밀번호 등록이 완료된 사원이라면 등록 불가. */
        if (!employee.getPassword().isEmpty()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_PASSWORD);
        }

        String pwd = passwordEncoder.encode(employeePwdRegisterDTO.getPassword());

        employee.registerPassword(pwd);

        employeeCommandRepository.save(employee);
    }

    public TokenResponseDTO reissueToken(String refreshToken) {

        String token = refreshToken.substring(7);

        String empId = jwtUtil.getUserId(token);
        Map<String, Object> claims = jwtUtil.parseClaims(token);

        /* whiteList 에 포함된 refreshToken 인지 확인 */
        whiteRefreshTokenRepository.findById(token).orElseThrow(() -> new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN));

        String newAccessToken = jwtTokenProvider.createAccessToken(empId, claims);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(empId, claims);

        /* refreshToken 화이트 리스트로 redis에 저장 */
        whiteRefreshTokenRepository.save(new WhiteRefreshToken(newRefreshToken, empId));

        return new TokenResponseDTO(empId, newAccessToken, newRefreshToken);
    }

    public void logoutRequestEmployee(EmployeeLogoutRequestDTO logoutRequestDTO) {

        /* whiteList에서 사용자의 최신 RefreshToken을 삭제 */
        whiteRefreshTokenRepository.deleteById(logoutRequestDTO.getEmpId());

        /* blackList에 사용자의 최신 accessToken을 등록 */
        blackAccessTokenRepository.save(new BlackAccessToken(logoutRequestDTO.getAccessToken(), logoutRequestDTO.getEmpId()));

    }

    private boolean isCSV(String extension) {

        return extension.equals("csv") || extension.equals("CSV");
    }

    // csv에서 추출한 데이터로 Employee 엔터티 리스트 생성하여 반환하는 메소드
    private List<Employee> getEmpList(MultipartFile empCSV) {

        List<Employee> empList = new ArrayList<>();

        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(empCSV.getInputStream()));

            List<String[]> rowList = csvReader.readAll();

            for (int i = 1; i < rowList.size(); i++) {

                String[] row = rowList.get(i);

                EmployeeRegisterDTO employeeRegisterDTO = getEmployeeRegisterDTO(row);

                Department department = departmentCommandRepository.findById(employeeRegisterDTO.getDepartmentId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));

                Position position = positionCommandRepository.findById(employeeRegisterDTO.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));
                Job job = jobCommandRepository.findById(employeeRegisterDTO.getJobId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_JOB));

                Employee newEmployee = EmployeeMapper.toEntity(employeeRegisterDTO, position, job, department);

                empList.add(newEmployee);
            }
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAIL_READ_FILE);
        } catch (CsvException e) {
            throw new CustomException(ErrorCode.NOT_MATCHED_CSV);
        }
        return empList;
    }

    // 사원 등록 DTO 생성 함수
    public EmployeeRegisterDTO getEmployeeRegisterDTO(String[] row) {

        return EmployeeRegisterDTO.builder()
                .empId(row[0])
                .positionId(Long.valueOf(row[1]))
                .jobId(Long.valueOf(row[2]))
                .departmentId(Long.valueOf(row[3]))
                .name(row[4])
                .gender(row[5])
                .rrn(row[6])
                .pay(Long.valueOf(row[7]))
                .address(row[8])
                .contact(row[9])
                .email(row[10])
                .joinDate(LocalDate.parse(row[11]))
                .Status(EmployeeStatus.valueOf(row[12]))
                .build();
    }

    public void sendInvitationEmail(Employee newEmployee) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("empId", newEmployee.getEmpId());

        String emailToken = jwtTokenProvider.createEmailToken(newEmployee.getEmpId(), claims);

        emailService.sendStyledEmail(newEmployee.getEmail(), newEmployee.getName(), emailToken);
    }
}
