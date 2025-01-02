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
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeOvertimeRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeOvertimeCommandRepository;
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
class WorkAttitudeOvertimeCommandServiceTest {

    @Mock
    private WorkAttitudeOvertimeCommandRepository overtimeRepository;
    @Mock
    private EmployeeCommandRepository employeeRepository;

    @InjectMocks
    private WorkAttitudeOvertimeCommandService service;

    private Employee mockEmployee;
    private Employee mockApprover;
    private Overtime mockOvertime;
    private WorkAttitudeOvertimeRequestDTO requestDTO;

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

//        mockEmployee = new Employee(
//                employeeDTO,
//                mock(Position.class),
//                mock(Job.class),
//                mock(Department.class)
//        );
        mockEmployee = Employee.builder()
                .registerDTO(employeeDTO)
                .position(mock(Position.class))
                .job(mock(Job.class))
                .department(mock(Department.class))
                .build();

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

        mockEmployee = Employee.builder()
                .registerDTO(approverDTO)
                .position(mock(Position.class))
                .job(mock(Job.class))
                .department(mock(Department.class))
                .build();

        requestDTO = new WorkAttitudeOvertimeRequestDTO();
        requestDTO.setOvertimeStart(LocalDateTime.of(2024, 6, 1, 18, 0));
        requestDTO.setOvertimeEnd(LocalDateTime.of(2024, 6, 2, 6, 0));
        requestDTO.setOvertimeType(OvertimeType.NIGHT);
        requestDTO.setOvertimeRetroactiveReason("Reason for overtime");
        requestDTO.setIsOvertimeRetroactive(true);
        requestDTO.setApproverId("EMP002");

        mockOvertime = Overtime.builder()
                .empId(mockEmployee)
                .approver(mockApprover)
                .overtimeType(OvertimeType.NIGHT)
                .overtimeStart(LocalDateTime.of(2024, 6, 1, 18, 0))
                .overtimeEnd(LocalDateTime.of(2024, 6, 2, 6, 0))
                .overtimeStatus(Status.PENDING)
                .build();
    }

    @Nested
    @DisplayName("createOvertime() Tests")
    class CreateOvertimeTests {

        @Test
        @DisplayName("초과근무 신청 성공")
        void createOvertime_Success() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.of(mockApprover));
                given(overtimeRepository.existsByEmpIdAndStatusAndOvertimeStartLessThanEqualAndOvertimeEndGreaterThanEqual(
                        anyString(), any(Status.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(false);

                service.createOvertime(requestDTO);

                then(overtimeRepository).should(times(1)).save(any(Overtime.class));
            }
        }

        /*@Test
        @DisplayName("중복 초과근무 예외")
        void createOvertime_DuplicateOvertime() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(overtimeRepository.existsByEmpIdAndStatusAndOvertimeStartLessThanEqualAndOvertimeEndGreaterThanEqual(
                        anyString(), any(Status.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(true);

                CustomException exception = assertThrows(CustomException.class, () -> service.createOvertime(requestDTO));
                assertEquals(ErrorCode.DUPLICATE_OVERTIME, exception.getErrorCode());
            }
        }*/

        /*@Test
        @DisplayName("잘못된 야간 근무 요청 예외")
        void createOvertime_InvalidNightRequest() {
            requestDTO.setOvertimeStart(LocalDateTime.of(2024, 6, 1, 15, 0));
            requestDTO.setOvertimeEnd(LocalDateTime.of(2024, 6, 1, 17, 0));

            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));

                CustomException exception = assertThrows(CustomException.class, () -> service.createOvertime(requestDTO));
                assertEquals(ErrorCode.INVALID_OVERTIME_REQUEST, exception.getErrorCode());
            }
        }*/

        @Test
        @DisplayName("결재자 정보 없음 예외")
        void createOvertime_ApproverNotFound() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.empty());

                CustomException exception = assertThrows(CustomException.class, () -> service.createOvertime(requestDTO));
                assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, exception.getErrorCode());
            }
        }
    }
}
