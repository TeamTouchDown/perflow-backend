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
import com.touchdown.perflowbackend.workAttitude.command.application.dto.WorkAttitudeTravelRequestDTO;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeTravelCommandRepository;
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
class WorkAttitudeTravelCommandServiceTest {

    @Mock
    private WorkAttitudeTravelCommandRepository travelRepository;
    @Mock
    private EmployeeCommandRepository employeeRepository;

    @InjectMocks
    private WorkAttitudeTravelCommandService service;

    private Employee mockEmployee;
    private Employee mockApprover;
    private Travel mockTravel;
    private WorkAttitudeTravelRequestDTO requestDTO;

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

        mockApprover = Employee.builder()
                .registerDTO(approverDTO)
                .position(mock(Position.class))
                .job(mock(Job.class))
                .department(mock(Department.class))
                .build();

        requestDTO = new WorkAttitudeTravelRequestDTO();
        requestDTO.setTravelStart(LocalDateTime.of(2024, 6, 1, 9, 0));
        requestDTO.setTravelEnd(LocalDateTime.of(2024, 6, 5, 18, 0));
        requestDTO.setTravelReason("Business Meeting");
        requestDTO.setTravelDivision("International");
        requestDTO.setApproverId("EMP002");

        mockTravel = new Travel(
                mockEmployee,
                mockApprover,
                LocalDateTime.now(),
                "Business Meeting",
                LocalDateTime.of(2024, 6, 1, 9, 0),
                LocalDateTime.of(2024, 6, 5, 18, 0),
                Status.PENDING,
                null,
                "International",
                Status.ACTIVATED
        );
    }

    @Nested
    @DisplayName("requestTravel() Tests")
    class RequestTravelTests {

        @Test
        @DisplayName("출장 신청 성공")
        void requestTravel_Success() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.of(mockApprover));
                given(travelRepository.existsByEmployee_EmpIdAndStatusNotAndTravelEndGreaterThanEqualAndTravelStartLessThanEqual(
                        anyString(), any(Status.class), any(LocalDateTime.class), any(LocalDateTime.class))).willReturn(false);

                service.requestTravel(requestDTO);

                then(travelRepository).should(times(1)).save(any(Travel.class));
            }
        }

        @Test
        @DisplayName("중복 출장 예외")
        void requestTravel_DuplicateTravel() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                // Mock 설정 강화
                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.of(mockApprover)); // 결재자 정보 추가
                given(travelRepository.existsByEmployee_EmpIdAndStatusNotAndTravelEndGreaterThanEqualAndTravelStartLessThanEqual(
                        eq("EMP001"), any(Status.class), any(LocalDateTime.class), any(LocalDateTime.class)))
                        .willReturn(true); // 중복 조건 활성화

                // DTO 검증 추가
                assertEquals("EMP002", requestDTO.getApproverId());

                // 테스트 실행
                CustomException exception = assertThrows(CustomException.class, () -> service.requestTravel(requestDTO));

                // 예외 검증
                assertEquals(ErrorCode.DUPLICATE_TRAVEL, exception.getErrorCode());
            }
        }


        @Test
        @DisplayName("결재자 정보 없음 예외")
        void requestTravel_ApproverNotFound() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(employeeRepository.findById("EMP002")).willReturn(Optional.empty());

                CustomException exception = assertThrows(CustomException.class, () -> service.requestTravel(requestDTO));
                assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, exception.getErrorCode());
            }
        }
    }
}
