package com.touchdown.perflowbackend.workAttitude.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.common.util.QRCodeGenerator;
import com.touchdown.perflowbackend.employee.command.application.dto.EmployeeCreateDTO;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.repository.EmployeeCommandRepository;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AttendanceStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAttendanceCommandRepository;
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
class WorkAttitudeAttendanceCommandServiceTest {

    @Mock
    private WorkAttitudeAttendanceCommandRepository attendanceRepository;
    @Mock
    private EmployeeCommandRepository employeeRepository;
    @Mock
    private QRCodeGenerator qrCodeGenerator;

    @InjectMocks
    private WorkAttitudeAttendanceCommandService service;

    private Employee mockEmployee;
    private Attendance mockAttendance;

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

        mockAttendance = Attendance.builder()
                .empId(mockEmployee)
                .checkInDateTime(LocalDateTime.now())
                .attendanceStatus(AttendanceStatus.WORK)
                .build();
    }

    @Nested
    @DisplayName("generateQRCodeForCheckIn() Tests")
    class GenerateQRCodeForCheckInTests {

        @Test
        @DisplayName("성공적으로 QR 코드 생성")
        void generateQRCodeForCheckIn_Success() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(attendanceRepository.existsByEmpIdAndCheckOutDateTimeIsNull(mockEmployee)).willReturn(false);
                given(qrCodeGenerator.generateQRCode("EMP001")).willReturn("QRCode123");

                String qrCode = service.generateQRCodeForCheckIn();

                assertEquals("QRCode123", qrCode);
            }
        }

        @Test
        @DisplayName("이미 출근한 경우 예외 발생")
        void generateQRCodeForCheckIn_AlreadyCheckedIn() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(attendanceRepository.existsByEmpIdAndCheckOutDateTimeIsNull(mockEmployee)).willReturn(true);

                CustomException exception = assertThrows(CustomException.class, () -> service.generateQRCodeForCheckIn());
                assertEquals(ErrorCode.ALREADY_CHECKED_IN, exception.getErrorCode());
            }
        }

        @Test
        @DisplayName("사원 정보 없음 예외 발생")
        void generateQRCodeForCheckIn_EmployeeNotFound() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.empty());

                CustomException exception = assertThrows(CustomException.class, () -> service.generateQRCodeForCheckIn());
                assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, exception.getErrorCode());
            }
        }

        @Test
        @DisplayName("출근 상태 중복 검증")
        void generateQRCodeForCheckIn_DuplicateAttendanceCheck() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(attendanceRepository.existsByEmpIdAndCheckOutDateTimeIsNull(mockEmployee)).willReturn(true);

                CustomException exception = assertThrows(CustomException.class, () -> service.generateQRCodeForCheckIn());
                assertEquals(ErrorCode.ALREADY_CHECKED_IN, exception.getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("validateAndCheckIn() Tests")
    class ValidateAndCheckInTests {

        @Test
        @DisplayName("QR 코드 검증 및 출근 성공")
        void validateAndCheckIn_Success() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(qrCodeGenerator.validateQRCode("EMP001", "QRCode123")).willReturn(true);

                service.validateAndCheckIn("QRCode123");

                then(attendanceRepository).should(times(1)).save(any(Attendance.class));
            }
        }

        @Test
        @DisplayName("잘못된 QR 코드 예외 발생")
        void validateAndCheckIn_InvalidQRCode() {
            try (MockedStatic<EmployeeUtil> mockedEmployeeUtil = Mockito.mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001")).willReturn(Optional.of(mockEmployee));
                given(qrCodeGenerator.validateQRCode("EMP001", "InvalidQRCode")).willReturn(false);

                CustomException exception = assertThrows(CustomException.class, () -> service.validateAndCheckIn("InvalidQRCode"));
                assertEquals(ErrorCode.INVALID_QR_CODE, exception.getErrorCode());
            }
        }
    }
}
