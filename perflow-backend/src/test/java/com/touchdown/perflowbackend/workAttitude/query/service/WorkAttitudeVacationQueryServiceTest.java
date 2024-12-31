package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.VacationStatus;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.VacationType;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeVacationResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeVacationQueryRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class WorkAttitudeVacationQueryServiceTest {

    @Mock
    private WorkAttitudeVacationQueryRepository vacationQueryRepository;

    @InjectMocks
    private WorkAttitudeVacationQueryService service;

    private WorkAttitudeVacationResponseDTO mockVacationResponse;

    @BeforeEach
    void setUp() {
        mockVacationResponse = WorkAttitudeVacationResponseDTO.builder()
                .empId("EMP001")
                .vacationType(VacationType.FAMILYCARE)
                .vacationStart(LocalDateTime.of(2024, 6, 1, 9, 0))
                .vacationEnd(LocalDateTime.of(2024, 6, 5, 18, 0))
                .vacationStatus(VacationStatus.CONFIRMED)
                .build();
    }


    @Nested
    @DisplayName("getVacationUsageDetails() Tests")
    class GetVacationUsageDetailsTests {

        @Test
        @DisplayName("휴가 사용 내역 조회 성공")
        void getVacationUsageDetails_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(vacationQueryRepository.findByEmpId("EMP001"))
                        .willReturn(List.of(mockVacationResponse));

                Map<String, Object> result = service.getVacationUsageDetails();

                assertNotNull(result);
                assertEquals("EMP001", result.get("empId"));
                assertTrue(result.containsKey("usageDetails"));
            }
        }
    }

    @Nested
    @DisplayName("getVacationDetails() Tests")
    class GetVacationDetailsTests {

        @Test
        @DisplayName("휴가 상세 내역 조회 성공")
        void getVacationDetails_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(vacationQueryRepository.findDetailsByEmpId("EMP001"))
                        .willReturn(List.of(mockVacationResponse));

                List<WorkAttitudeVacationResponseDTO> result = service.getVacationDetails();

                assertNotNull(result);
                assertEquals(1, result.size());
                assertEquals(mockVacationResponse, result.get(0));
            }
        }
    }

    @Nested
    @DisplayName("getNearestVacation() Tests")
    class GetNearestVacationTests {

        @Test
        @DisplayName("가장 가까운 휴가 조회 성공")
        void getNearestVacation_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(vacationQueryRepository.findNearestVacation(eq("EMP001"), any(LocalDateTime.class)))
                        .willReturn(Optional.of(mockVacationResponse));

                Map<String, Object> result = service.getNearestVacation();

                assertNotNull(result);
                assertEquals("EMP001", result.get("empId"));
                assertTrue(result.containsKey("nearestVacation"));
                assertTrue(result.containsKey("daysUntilVacation"));
            }
        }

        @Test
        @DisplayName("가장 가까운 휴가 없음 예외")
        void getNearestVacation_NotFound() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(vacationQueryRepository.findNearestVacation(eq("EMP001"), any(LocalDateTime.class)))
                        .willReturn(Optional.empty());

                CustomException exception = assertThrows(CustomException.class, () -> service.getNearestVacation());
                assertEquals(ErrorCode.NOT_FOUND_VACATION, exception.getErrorCode());
            }
        }
    }

}
