package com.touchdown.perflowbackend.workAttitude.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.security.util.EmployeeUtil;
import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeTravelResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.repository.WorkAttitudeTravelQueryRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class WorkAttitudeTravelQueryServiceTest {

    @Mock
    private WorkAttitudeTravelQueryRepository travelQueryRepository;

    @InjectMocks
    private WorkAttitudeTravelQueryService service;

    private WorkAttitudeTravelResponseDTO mockTravelResponse;

    @BeforeEach
    void setUp() {
        mockTravelResponse = WorkAttitudeTravelResponseDTO.builder()
                .travelId(1L)
                .empId("EMP001")
                .approverId("EMP002")
                .approverName("Jane Approver")
                .travelReason("Business Trip")
                .travelStart(LocalDateTime.of(2024, 6, 1, 9, 0))
                .travelEnd(LocalDateTime.of(2024, 6, 5, 18, 0))
                .travelStatus("PENDING")
                .travelDivision("International")
                .travelRejectReason(null)
                .createDatetime(LocalDateTime.now())
                .updateDatetime(LocalDateTime.now())
                .status(com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status.ACTIVATED)
                .build();
    }

    @Nested
    @DisplayName("getTravelsForEmployee() Tests")
    class GetTravelsForEmployeeTests {

        @Test
        @DisplayName("직원 출장 조회 성공")
        void getTravelsForEmployee_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP001");

                given(travelQueryRepository.findByEmployee_EmpIdAndStatusNot("EMP001", com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status.DELETED))
                        .willReturn(List.of());

                List<WorkAttitudeTravelResponseDTO> result = service.getTravelsForEmployee();

                assertNotNull(result);
                assertEquals(0, result.size());
            }
        }
    }

    @Nested
    @DisplayName("getAllTravelsForLeader() Tests")
    class GetAllTravelsForLeaderTests {

        @Test
        @DisplayName("리더 출장 조회 성공")
        void getAllTravelsForLeader_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP002");

                given(travelQueryRepository.findByApprover_EmpIdAndStatusNot("EMP002", com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status.DELETED))
                        .willReturn(List.of());

                List<WorkAttitudeTravelResponseDTO> result = service.getAllTravelsForLeader();

                assertNotNull(result);
                assertEquals(0, result.size());
            }
        }
    }

    @Nested
    @DisplayName("getPendingTravelsForLeader() Tests")
    class GetPendingTravelsForLeaderTests {

        @Test
        @DisplayName("리더 대기 중인 출장 조회 성공")
        void getPendingTravelsForLeader_Success() {
            try (var mockedEmployeeUtil = mockStatic(EmployeeUtil.class)) {
                mockedEmployeeUtil.when(EmployeeUtil::getEmpId).thenReturn("EMP002");

                given(travelQueryRepository.findByApprover_EmpIdAndTravelStatusAndStatusNot("EMP002", com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status.PENDING, com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status.DELETED))
                        .willReturn(List.of());

                List<WorkAttitudeTravelResponseDTO> result = service.getPendingTravelsForLeader();

                assertNotNull(result);
                assertEquals(0, result.size());
            }
        }
    }

    @Nested
    @DisplayName("getAllTravelsForHR() Tests")
    class GetAllTravelsForHRTests {

        @Test
        @DisplayName("HR 전체 출장 조회 성공")
        void getAllTravelsForHR_Success() {
            given(travelQueryRepository.findByStatusNot(com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status.DELETED))
                    .willReturn(List.of());

            List<WorkAttitudeTravelResponseDTO> result = service.getAllTravelsForHR();

            assertNotNull(result);
            assertEquals(0, result.size());
        }
    }
}
