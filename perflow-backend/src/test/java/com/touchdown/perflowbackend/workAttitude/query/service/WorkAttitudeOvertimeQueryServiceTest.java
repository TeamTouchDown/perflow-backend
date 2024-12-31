/*
package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeOvertimeResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeOvertimeQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class WorkAttitudeOvertimeQueryServiceTest {

    @Mock
    private WorkAttitudeOvertimeQueryRepository overtimeQueryRepository;

    @InjectMocks
    private WorkAttitudeOvertimeQueryService service;

    private Overtime mockOvertime;

    @BeforeEach
    void setUp() {
        // Position, Job, Department 더미 데이터 설정
        Position mockPosition = new Position(1L, "Manager");
        Job mockJob = new Job(1L, "Engineer");
        Department mockDepartment = new Department(1L, "HR");

        // Employee 객체 수동 생성
        Employee mockEmployee = new Employee(
                "EMP001", // empId
                mockPosition,
                mockJob,
                mockDepartment,
                "password123",
                "John Employee",
                "Male",
                "123456-7890123",
                500000L,
                "123 Street",
                "010-1234-5678",
                "employee@example.com",
                null, // seal
                LocalDate.of(2020, 1, 1),
                EmployeeStatus.PENDING,
                null,
                new LinkedHashSet<>()
        );

        Employee mockApprover = new Employee(
                "EMP002",
                mockPosition,
                mockJob,
                mockDepartment,
                "password456",
                "Jane Approver",
                "Female",
                "987654-3210987",
                600000L,
                "456 Avenue",
                "010-8765-4321",
                "approver@example.com",
                null, // seal
                LocalDate.of(2020, 1, 1),
                EmployeeStatus.PENDING,
                null,
                new LinkedHashSet<>()
        );

        // Overtime 객체 설정
        mockOvertime = Overtime.builder()
                .empId(mockEmployee)
                .approver(mockApprover)
                .overtimeType(OvertimeType.NIGHT)
                .enrollOvertime(LocalDateTime.of(2024, 6, 1, 9, 0))
                .overtimeStart(LocalDateTime.of(2024, 6, 1, 18, 0))
                .overtimeEnd(LocalDateTime.of(2024, 6, 2, 6, 0))
                .overtimeStatus(Status.PENDING)
                .status(Status.ACTIVATED)
                .build();
    }


    @Nested
    @DisplayName("getAllOvertimes() Tests")
    class GetAllOvertimesTests {

        @Test
        @DisplayName("전체 초과근무 조회 성공")
        void getAllOvertimes_Success() {
            given(overtimeQueryRepository.findAllNotDeleted()).willReturn(List.of(mockOvertime));

            List<WorkAttitudeOvertimeResponseDTO> result = service.getAllOvertimes();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("EMP001", result.get(0).getEmpId());
        }
    }

    @Nested
    @DisplayName("getOvertimeForEmployee() Tests")
    class GetOvertimeForEmployeeTests {

        @Test
        @DisplayName("직원 초과근무 조회 성공")
        void getOvertimeForEmployee_Success() {
            given(overtimeQueryRepository.findByEmpIdNotDeleted("EMP001"))
                    .willReturn(List.of(mockOvertime));

            List<WorkAttitudeOvertimeResponseDTO> result = service.getOvertimeForEmployee("EMP001");

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("EMP001", result.get(0).getEmpId());
        }

        @Test
        @DisplayName("직원 초과근무 조회 실패 - 권한 없음")
        void getOvertimeForEmployee_Unauthorized() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP002");

                CustomException exception = assertThrows(CustomException.class, () -> {
                    service.getOvertimeForEmployee("EMP001");
                });

                assertEquals(ErrorCode.UNAUTHORIZED_ACCESS, exception.getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("getOvertimeForTeam() Tests")
    class GetOvertimeForTeamTests {

        @Test
        @DisplayName("팀 초과근무 조회 성공")
        void getOvertimeForTeam_Success() {
            given(overtimeQueryRepository.findTeamOvertimes(any())).willReturn(List.of(mockOvertime));

            List<WorkAttitudeOvertimeResponseDTO> result = service.getOvertimeForTeam();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("EMP001", result.get(0).getEmpId());
        }
    }
}
*/
