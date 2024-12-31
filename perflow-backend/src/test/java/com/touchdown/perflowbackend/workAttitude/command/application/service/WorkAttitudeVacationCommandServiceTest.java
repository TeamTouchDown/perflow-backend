package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeVacationRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.*;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAnnualCommandRepository;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeVacationCommandRepository;
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
class WorkAttitudeVacationCommandServiceTest {

    @Mock
    private WorkAttitudeVacationCommandRepository vacationRepository;
    @Mock
    private EmployeeCommandRepository employeeRepository;
    @Mock
    private WorkAttitudeAnnualCommandRepository annualRepository;

    @InjectMocks
    private WorkAttitudeVacationCommandService service;

    private Employee mockEmployee;
    private Employee mockApprover;
    private Vacation mockVacation;
    private WorkAttitudeVacationRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
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

        mockEmployee = new Employee(
                employeeDTO,
                mock(Position.class),
                mock(Job.class),
                mock(Department.class)
        );

        EmployeeCreateDTO approverDTO = EmployeeCreateDTO.builder()
                .empId("EMP002")
                .name("Jane Approver")
                .gender("Female")
                .rrn("987654-3210987")
                .pay(600000L)
                .address("456 Avenue")
                .contact("010-8765-4321")
                .email("approver@example.com")
                .joinDate(LocalDate.of(2019, 1, 1))
                .build();

        mockApprover = new Employee(
                approverDTO,
                mock(Position.class),
                mock(Job.class),
                mock(Department.class)
        );

        requestDTO = new WorkAttitudeVacationRequestDTO();
        requestDTO.setVacationStart(LocalDateTime.of(2024, 6, 1, 9, 0));
        requestDTO.setVacationEnd(LocalDateTime.of(2024, 6, 5, 18, 0));
        requestDTO.setVacationType(VacationType.MENSTRUAL);
        requestDTO.setApprover("EMP002");

        mockVacation = Vacation.builder()
                .empId(mockEmployee)
                .approver(mockApprover)
                .enrollVacation(LocalDateTime.now())
                .vacationStart(LocalDateTime.of(2024, 6, 1, 9, 0))
                .vacationEnd(LocalDateTime.of(2024, 6, 5, 18, 0))
                .vacationType(VacationType.MENSTRUAL)
                .vacationStatus(VacationStatus.PENDING)
                .status(Status.ACTIVATED)
                .build();
    }

    @Nested
    @DisplayName("registerVacation() Tests")
    class RegisterVacationTests {

        @Test
        @DisplayName("휴가 신청 성공")
        void registerVacation_Success() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.of(mockApprover));
                given(vacationRepository.existsByEmpIdAndStatusAndVacationStartAndVacationEnd(
                        anyString(), eq(Status.ACTIVATED), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(false);
                given(annualRepository.existsByEmpId_EmpIdAndStatusAndAnnualStartLessThanEqualAndAnnualEndGreaterThanEqual(
                        anyString(), eq(Status.ACTIVATED), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(false);

                service.registerVacation(requestDTO);

                then(vacationRepository).should(times(1)).save(any(Vacation.class));
            }
        }



        @Test
        @DisplayName("결재자 정보 없음 예외")
        void registerVacation_ApproverNotFound() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.empty());

                CustomException exception = assertThrows(CustomException.class, () -> service.registerVacation(requestDTO));
                assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, exception.getErrorCode());
            }
        }
    }
}
