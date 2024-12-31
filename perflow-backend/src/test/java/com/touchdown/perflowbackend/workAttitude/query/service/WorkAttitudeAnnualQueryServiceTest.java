package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.AnnualType;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeAnnualResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeAnnualQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class WorkAttitudeAnnualQueryServiceTest {

    @Mock
    private WorkAttitudeAnnualQueryRepository annualRepository;

    @Mock
    private EmployeeQueryRepository employeeRepository;

    @InjectMocks
    private WorkAttitudeAnnualQueryService service;

    private WorkAttitudeAnnualResponseDTO mockAnnualResponse;

    @BeforeEach
    void setUp() {
        mockAnnualResponse = WorkAttitudeAnnualResponseDTO.builder()
                .annualId(1L)
                .empId("EMP001")
                .annualType(AnnualType.FULLDAY)
                .annualStart(LocalDateTime.of(2024, 6, 1, 9, 0))
                .annualEnd(LocalDateTime.of(2024, 6, 5, 18, 0))
                .status(Status.CONFIRMED)
                .build();
    }

    @Nested
    @DisplayName("getAnnualBalance() Tests")
    class GetAnnualBalanceTests {

        /*@Test
        @DisplayName("잔여 연차 조회 성공")
        void getAnnualBalance_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");
                given(employeeRepository.findJoinDateByEmpId("EMP001")).willReturn(Optional.of(LocalDate.of(2020, 1, 1)));
                given(annualRepository.findConfirmedAnnualsByYearAndEndDate("EMP001", 2024))
                        .willReturn(List.of(Annual.builder()
                                .annualType(AnnualType.FULLDAY)
                                .annualStart(LocalDateTime.of(2024, 6, 1, 9, 0))
                                .annualEnd(LocalDateTime.of(2024, 6, 1, 18, 0))
                                .build()));

                double balance = service.getAnnualBalance();

                assertEquals(14.0, balance); // 기본 15일 - 사용 1일 = 14일
            }
        }*/

        @Test
        @DisplayName("잔여 연차 조회 실패 - 사원 없음")
        void getAnnualBalance_Fail_EmployeeNotFound() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");
                given(employeeRepository.findJoinDateByEmpId("EMP001")).willReturn(Optional.empty());

                CustomException exception = assertThrows(CustomException.class, () -> {
                    service.getAnnualBalance();
                });
                assertEquals(ErrorCode.NOT_FOUND_EMPLOYEE, exception.getErrorCode());
            }
        }
    }

    @Nested
    @DisplayName("getAnnualList() Tests")
    class GetAnnualListTests {

        @Test
        @DisplayName("연차 목록 조회 성공")
        void getAnnualList_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");
                given(annualRepository.findByEmpId("EMP001")).willReturn(List.of(mockAnnualResponse));

                List<WorkAttitudeAnnualResponseDTO> result = service.getAnnualList();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals("EMP001", result.get(0).getEmpId());
            }
        }
    }

    @Nested
    @DisplayName("getAnnualUsageDetails() Tests")
    class GetAnnualUsageDetailsTests {

        @Test
        @DisplayName("연차 사용 내역 조회 성공")
        void getAnnualUsageDetails_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");
                given(annualRepository.findByEmpId("EMP001")).willReturn(List.of(mockAnnualResponse));

                Map<String, Object> result = service.getAnnualUsageDetails();

                assertNotNull(result);
                assertEquals(1, ((Map<String, Long>) result.get("usageDetails")).get(AnnualType.FULLDAY.toString()));
            }
        }
    }
}
