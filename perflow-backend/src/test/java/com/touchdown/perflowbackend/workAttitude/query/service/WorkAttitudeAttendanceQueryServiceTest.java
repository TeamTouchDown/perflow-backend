/*
package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AttendanceStatus;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAttendanceSummaryResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAttendanceQueryRepository;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class WorkAttitudeAttendanceQueryServiceTest {

    @Mock
    private WorkAttitudeAttendanceQueryRepository attendanceQueryRepository;

    @Mock
    private EmployeeQueryRepository employeeRepository; // 추가

    @InjectMocks
    private WorkAttitudeAttendanceQueryService service;

    private Attendance mockAttendance;

    @BeforeEach
    void setUp() {
        mockAttendance = Attendance.builder()
                .empId(Mockito.mock(com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee.class))
                .checkInDateTime(LocalDateTime.of(2024, 6, 1, 9, 0))
                .checkOutDateTime(LocalDateTime.of(2024, 6, 1, 18, 0))
                .attendanceStatus(AttendanceStatus.WORK)
                .build();
    }

    @Nested
    @DisplayName("getWeeklySummaryForEmployee() Tests")
    class GetWeeklySummaryForEmployeeTests {

        @Test
        @DisplayName("주차별 조회 성공")
        void getWeeklySummaryForEmployee_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001"))
                        .willReturn(Optional.of(Mockito.mock(com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee.class)));

                given(attendanceQueryRepository.findByEmpId("EMP001"))
                        .willReturn(List.of(mockAttendance));

                List<WorkAttitudeAttendanceSummaryResponseDTO> result = service.getWeeklySummaryForEmployee();

                assertNotNull(result);
                assertEquals(1, result.size());
            }
        }

        @Test
        @DisplayName("주차별 조회 실패 - 사원 없음")
        void getWeeklySummaryForEmployee_NotFound() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP999");

                given(attendanceQueryRepository.findByEmpId("EMP999")).willReturn(List.of());

                CustomException exception = assertThrows(CustomException.class, () -> {
                    service.getWeeklySummaryForEmployee();
                });

                assertEquals(ErrorCode.NOT_FOUND_EMP, exception.getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("getMonthlySummaryForEmployee() Tests")
    class GetMonthlySummaryForEmployeeTests {

        @Test
        @DisplayName("월별 조회 성공")
        void getMonthlySummaryForEmployee_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(employeeRepository.findById("EMP001"))
                        .willReturn(Optional.of(Mockito.mock(com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee.class)));

                given(attendanceQueryRepository.findByEmpId("EMP001"))
                        .willReturn(List.of(mockAttendance));

                List<WorkAttitudeAttendanceSummaryResponseDTO> result = service.getMonthlySummaryForEmployee();

                assertNotNull(result);
                assertEquals(1, result.size());
            }
        }
    }

    @Nested
    @DisplayName("getWeeklySummaryForTeam() Tests")
    class GetWeeklySummaryForTeamTests {

        @Test
        @DisplayName("팀 주차별 조회 성공")
        void getWeeklySummaryForTeam_Success() {
            given(attendanceQueryRepository.findByEmpIds(any())).willReturn(List.of(mockAttendance));

            List<WorkAttitudeAttendanceSummaryResponseDTO> result = service.getWeeklySummaryForTeam();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("getMonthlySummaryForAllEmployees() Tests")
    class GetMonthlySummaryForAllEmployeesTests {

        @Test
        @DisplayName("전체 사원 월별 조회 성공")
        void getMonthlySummaryForAllEmployees_Success() {
            given(attendanceQueryRepository.findAll()).willReturn(List.of(mockAttendance));

            List<WorkAttitudeAttendanceSummaryResponseDTO> result = service.getMonthlySummaryForAllEmployees();

            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }
}
*/
