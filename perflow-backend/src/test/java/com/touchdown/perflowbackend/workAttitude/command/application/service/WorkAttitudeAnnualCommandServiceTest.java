package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeAnnualRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAnnualCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeVacationCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.mapper.WorkAttitudeAnnualMapper;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WorkAttitudeAnnualCommandServiceTest {

    @Mock
    private WorkAttitudeAnnualCommandRepository annualRepository;
    @Mock
    private EmployeeCommandRepository employeeRepository;
    @Mock
    private WorkAttitudeVacationCommandRepository vacationRepository;

    @InjectMocks
    private WorkAttitudeAnnualCommandService service;

    private Employee mockEmployee;
    private Employee mockApprover;
    private WorkAttitudeAnnualRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // DTO 생성
        EmployeeCreateDTO employeeDTO = EmployeeCreateDTO.builder()
                .empId("EMP001")
                .name("John Employee")
                .gender("Male")
                .rrn("123456-7890123")
                .pay(500000L)
                .address("123 Street")
                .contact("010-1234-5678")
                .email("employee@example.com")
                .joinDate(LocalDate.of(2020, 1, 1))
                .build();

        EmployeeCreateDTO approverDTO = EmployeeCreateDTO.builder()
                .empId("EMP002")
                .name("Jane Approver")
                .gender("Female")
                .rrn("987654-3210987")
                .pay(600000L)
                .address("456 Avenue")
                .contact("010-9876-5432")
                .email("approver@example.com")
                .joinDate(LocalDate.of(2019, 1, 1))
                .build();

        // Mock 객체로 Position, Job, Department 설정
        Position mockPosition = mock(Position.class);
        Job mockJob = mock(Job.class);
        Department mockDept = mock(Department.class);

        // Employee 객체 생성
        mockEmployee = Employee.builder()
                .registerDTO(employeeDTO)
                .position(mockPosition)
                .job(mockJob)
                .department(mockDept)
                .build();

        mockApprover = Employee.builder()
                .registerDTO(approverDTO)
                .position(mockPosition)
                .job(mockJob)
                .department(mockDept)
                .build();

        // 요청 DTO 생성
        requestDTO = WorkAttitudeAnnualRequestDTO.builder()
                .annualStart(LocalDateTime.of(2024, 5, 1, 9, 0))
                .annualEnd(LocalDateTime.of(2024, 5, 2, 18, 0))
                .annualType(AnnualType.FULLDAY)
                .approver("EMP002")
                .build();
    }


    @Nested
    @DisplayName("registerAnnual() Tests")
    class RegisterAnnualTests {

        @Test
        @DisplayName("연차 신청 성공 테스트")
        void registerAnnual_Success() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                // given
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.of(mockApprover));
                given(annualRepository.existsByEmpId_EmpIdAndStatusAndAnnualStartLessThanEqualAndAnnualEndGreaterThanEqual(
                        anyString(), any(Status.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(false);
                given(vacationRepository.existsByEmpIdAndStatusAndVacationStartAndVacationEnd(
                        anyString(), any(Status.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(false);

                // when
                service.registerAnnual(requestDTO);

                // then
                then(annualRepository).should(times(1)).save(any(Annual.class));
            }
        }

        @Test
        @DisplayName("사원 정보 없음 예외 테스트")
        void registerAnnual_EmployeeNotFound() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                // given
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");
                given(employeeRepository.findById("EMP001")).willReturn(Optional.empty());

                // when & then
                CustomException exception = assertThrows(CustomException.class, () -> service.registerAnnual(requestDTO));
                assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, exception.getErrorCode());
            }
        }

        @Test
        @DisplayName("날짜 중복 예외 테스트")
        void registerAnnual_DateOverlap() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                // given
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");
                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.of(mockApprover));
                given(annualRepository.existsByEmpId_EmpIdAndStatusAndAnnualStartLessThanEqualAndAnnualEndGreaterThanEqual(
                        eq("EMP001"), eq(Status.ACTIVATED), any(LocalDateTime.class), any(LocalDateTime.class)))
                        .willReturn(true);

                // when & then
                CustomException exception = assertThrows(CustomException.class, () -> service.registerAnnual(requestDTO));
                assertEquals(ErrorCode.DUPLICATE_ANNUAL, exception.getErrorCode());
            }
        }

        @Test
        @DisplayName("결재자 정보 없음 예외 테스트")
        void registerAnnual_ApproverNotFound() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");
                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.empty());

                CustomException exception = assertThrows(CustomException.class, () -> service.registerAnnual(requestDTO));
                assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, exception.getErrorCode());
            }
        }

        @Test
        @DisplayName("잔여 연차 부족 예외 테스트")
        void registerAnnual_InsufficientAnnualLeave() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.of(mockApprover));
                given(annualRepository.countByEmpId_EmpIdAndStatus(eq("EMP001"), eq(Status.CONFIRMED))).willReturn(15L);

                CustomException exception = assertThrows(CustomException.class, () -> service.registerAnnual(requestDTO));
                assertEquals(ErrorCode.NOT_ENOUGH_ANNUAL, exception.getErrorCode());
            }
        }

    }
}
