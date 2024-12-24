package com.touchdown.perflowbackend.employee.command.application.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.touchdown.perflowbackend.authority.domain.aggregate.AuthType;
import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import com.touchdown.perflowbackend.authority.domain.aggregate.AuthorityEmployee;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityEmployeeRepository;
import com.touchdown.perflowbackend.authority.domain.repository.AuthorityRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.*;
import com.touchdown.perflowbackend.employee.command.application.mapper.EmployeeMapper;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.file.command.application.service.FileService;
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
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.FileNameUtils;
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
    private final AuthorityRepository authorityRepository;
    private final AuthorityEmployeeRepository authorityEmployeeRepository;
    private final FileService fileService;

    private final AuthenticationManager authenticationManager;
    private final EntityManager entityManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createEmployee(EmployeeCreateDTO employeeCreateDTO) {

        Department department = departmentCommandRepository.findById(employeeCreateDTO.getDepartmentId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
        Position position = positionCommandRepository.findById(employeeCreateDTO.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));
        Job job = jobCommandRepository.findById(employeeCreateDTO.getJobId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_JOB));

        Employee newEmployee = EmployeeMapper.toEntity(employeeCreateDTO, position, job, department);

        entityManager.persist(newEmployee);
        employeeCommandRepository.save(newEmployee);

        Authority auth = authorityRepository.findByType(AuthType.EMPLOYEE);

        AuthorityEmployee authorityEmployee = AuthorityEmployee.builder()
                .authority(auth)
                .emp(newEmployee)
                .build();

        authorityEmployeeRepository.save(authorityEmployee);
        entityManager.flush();

        sendInvitationEmail(newEmployee);
    }

    @Transactional
    public void createEmployeeList(MultipartFile empCSV) {

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
    public void updateEmployee(EmployeeUpdateRequestDTO employeeUpdateRequestDTO) {

        Employee employee = findEmpById(employeeUpdateRequestDTO.getEmpId());

        employee.updateEmployee(employeeUpdateRequestDTO);

        employeeCommandRepository.save(employee);
    }

    @Transactional
    public void updateMyInfo(String empId, MyInfoUpdateDTO myInfoUpdateDTO) {

        Employee employee = findEmpById(empId);

        employee.updateMyInfo(myInfoUpdateDTO);

        employeeCommandRepository.save(employee);
    }

    @Transactional
    public void updateEmployeeStatus(EmployeeStatusUpdateDTO employeeStatusUpdateDTO) {

        Employee employee = findEmpById(employeeStatusUpdateDTO.getEmpId());

        employee.updateStatus(employeeStatusUpdateDTO.getStatus());

        employeeCommandRepository.save(employee);
    }

    @Transactional
    public TokenResponseDTO loginRequestEmployee(EmployeeLoginRequestDTO employeeLoginRequestDTO) {

        // 1. AuthenticationManager를 통해 인증 수행
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(employeeLoginRequestDTO.getEmpId(), employeeLoginRequestDTO.getPassword()));

        CustomEmployDetail customEmployDetail = (CustomEmployDetail) authentication.getPrincipal();

        String empId = customEmployDetail.getUsername();
        EmployeeStatus status = customEmployDetail.getStatus();
        String empName = customEmployDetail.getEmployeeName();

        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", empId);
        claims.put("status", status.name());
        claims.put("name", empName);

        String accessToken = jwtTokenProvider.createAccessToken(customEmployDetail.getUsername(), claims);
        String refreshToken = jwtTokenProvider.createRefreshToken(customEmployDetail.getUsername(), claims);

        /* refreshToken 화이트 리스트로 redis에 저장 */
        whiteRefreshTokenRepository.save(new WhiteRefreshToken(refreshToken, empId, 604800000L));

        return new TokenResponseDTO(empId, accessToken, refreshToken);
    }

    @Transactional
    public void createEmployeePassword(EmployeePwdCreateDTO employeePwdCreateDTO) {

        Employee employee = findEmpById(employeePwdCreateDTO.getEmpId());
        /* 이미 초기 비밀번호 등록이 완료된 사원이라면 등록 불가. */
        if (employee.getPassword() != null) {
            throw new CustomException(ErrorCode.ALREADY_REGISTERED_PASSWORD);
        }

        String pwd = passwordEncoder.encode(employeePwdCreateDTO.getPassword());

        employee.registerPassword(pwd);
        employee.updateStatus(EmployeeStatus.ACTIVE);

        employeeCommandRepository.save(employee);
    }

    @Transactional
    public void uploadEmployeeSeal(String empId, MultipartFile seal) {

        Employee employee = findEmpById(empId);

        String sealUrl = fileService.uploadSeal(seal);

        employee.updateSeal(sealUrl);

        employeeCommandRepository.save(employee);
    }

    @Transactional
    public TokenResponseDTO reissueToken(String refreshToken) {

        String token = refreshToken.substring(7);

        String empId = jwtUtil.getUserId(token);
        Map<String, Object> claims = jwtUtil.parseClaims(token);

        /* whiteList 에 포함된 refreshToken 인지 확인 */
        whiteRefreshTokenRepository.findById(empId).orElseThrow(() -> new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN));

        String newAccessToken = jwtTokenProvider.createAccessToken(empId, claims);
        String newRefreshToken = jwtTokenProvider.createRefreshToken(empId, claims);

        /* refreshToken 화이트 리스트로 redis에 저장 */
        whiteRefreshTokenRepository.save(new WhiteRefreshToken(newRefreshToken, empId, 604800000L));

        return new TokenResponseDTO(empId, newAccessToken, newRefreshToken);
    }

    public void logoutRequestEmployee(EmployeeLogoutRequestDTO logoutRequestDTO) {

        /* whiteList에서 사용자의 최신 RefreshToken을 삭제 */
        whiteRefreshTokenRepository.deleteById(logoutRequestDTO.getEmpId());

        /* blackList에 사용자의 최신 accessToken을 등록 */
        blackAccessTokenRepository.save(new BlackAccessToken(logoutRequestDTO.getAccessToken(), logoutRequestDTO.getEmpId(), 1800000L));

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

                EmployeeCreateDTO employeeCreateDTO = getEmployeeCreateDTO(row);

                Department department = departmentCommandRepository.findById(employeeCreateDTO.getDepartmentId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));

                Position position = positionCommandRepository.findById(employeeCreateDTO.getPositionId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POSITION));
                Job job = jobCommandRepository.findById(employeeCreateDTO.getJobId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_JOB));

                Employee newEmployee = EmployeeMapper.toEntity(employeeCreateDTO, position, job, department);

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
    public EmployeeCreateDTO getEmployeeCreateDTO(String[] row) {

        return EmployeeCreateDTO.builder()
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
                .build();
    }

    public void sendInvitationEmail(Employee newEmployee) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("empId", newEmployee.getEmpId());

        String emailToken = jwtTokenProvider.createEmailToken(newEmployee.getEmpId(), claims);

        emailService.sendStyledEmail(newEmployee.getEmail(), newEmployee.getName(), emailToken);
    }

    public Employee findEmpById(String empId) {

        return employeeCommandRepository.findById(empId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMP)
        );
    }

    public void redirectToPwdRegist(String token) {

        if(!jwtUtil.validateToken(token)) {
            throw new CustomException(ErrorCode.NOT_VALID_ACCESS_TOKEN);
        }
    }
}
