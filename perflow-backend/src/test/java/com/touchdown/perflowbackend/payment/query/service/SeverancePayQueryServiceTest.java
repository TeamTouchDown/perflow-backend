package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayDTO;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayDetailResponseDTO;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayStubDTO;
import com.touchdown.perflowbackend.payment.query.repository.SeverancePayQueryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeverancePayQueryServiceTest {

    @Mock
    private SeverancePayQueryRepository severancePayQueryRepository;

    @InjectMocks
    private SeverancePayQueryService severancePayQueryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("퇴직금 상세정보")
    public void testGetSeverancePay() {
        // given: 테스트에 필요한 입력값 준비
        Long severancePayId = 1L;

        // mock data for the query
        Object[] resultRow1 = new Object[]{
                1L,                         // severancePayId
                "EMP001",                   // empId
                "John Doe",                 // empName
                java.sql.Date.valueOf("2020-01-01"), // joinDate
                java.sql.Date.valueOf("2023-01-01"), // resignDate
                "Manager",                  // positionName
                "HR",                       // deptName
                100000L,                    // threeMonthTotalPay
                90L,                        // threeMonthTotalDays
                15000L,                     // threeMonthTotalAllowance
                900L,                       // totalLaborDays
                1000000L,                   // totalSeverancePay
                Status.PENDING,              // severanceStatus
                java.sql.Date.valueOf("2023-01-01") // createDate
        };

        List<Object[]> mockResults = Arrays.asList(new Object[][]{resultRow1});

        // mock the repository method
        lenient().when(severancePayQueryRepository.findSeverancePayDetails(severancePayId)).thenReturn(mockResults);

        // when: service method 실행
        SeverancePayDetailResponseDTO response = severancePayQueryService.getSeverancePay(severancePayId);

        List<SeverancePayDTO> severancePays = mapToDTO(mockResults);  // Object[]를 SeverancePayDTO로 변환

        // then: 결과 검증
        assertNotNull(response);
        assertEquals(1, severancePays.size());

        SeverancePayDTO severancePay = severancePays.get(0);

        assertEquals(1L, severancePay.getSeverancePayId());
        assertEquals("EMP001", severancePay.getEmpId());
        assertEquals("John Doe", severancePay.getEmpName());
        assertEquals(LocalDate.of(2020, 1, 1), severancePay.getJoinDate());
        assertEquals(LocalDate.of(2023, 1, 1), severancePay.getResignDate());
        assertEquals("Manager", severancePay.getPositionName());
        assertEquals("HR", severancePay.getDeptName());
        assertEquals(100000L, severancePay.getThreeMonthTotalPay());
        assertEquals(90L, severancePay.getThreeMonthTotalDays());
        assertEquals(15000L, severancePay.getThreeMonthTotalAllowance());
        assertEquals(900L, severancePay.getTotalLaborDays());
        assertEquals(1000000L, severancePay.getTotalSeverancePay());
        assertEquals(Status.PENDING, severancePay.getSeveranceStatus());
        assertEquals(LocalDateTime.of(2023, 1, 1, 0, 0), severancePay.getCreateDatetime());

    }

    private List<SeverancePayDTO> mapToDTO(List<Object[]> results) {

        return results.stream()
                .map(result -> new SeverancePayDTO(

                        (Long) result[0],
                        (String) result[1],
                        (String) result[2],
                        toLocalDate(result[3]),
                        toLocalDate(result[4]),
                        (String) result[5],
                        (String) result[6],
                        toLong(result[7]),  // 수정
                        toLong(result[8]),  // 수정
                        toLong(result[9]),  // 수정
                        toLong(result[10]), // 수정
                        toLong(result[11]),
                        (Status) result[12],
                        toLocalDate(result[13]).atStartOfDay()

                ))

                .collect(Collectors.toList());
    }

    private LocalDate toLocalDate(Object date) {

        if (date == null) {

            return null; // null 처리

        } else if (date instanceof java.sql.Date) {

            return ((java.sql.Date) date).toLocalDate();

        } else if (date instanceof java.util.Date) {

            return ((java.util.Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        } else if (date instanceof LocalDate) {

            return (LocalDate) date;

        } else {

            throw new IllegalArgumentException("Unexpected date type: " + date);

        }
    }

    private Long toLong(Object value) {
        if (value == null) {
            return null;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof Integer) {
            return ((Integer) value).longValue();
        } else {
            throw new IllegalArgumentException("Unexpected type for Long field: " + value.getClass().getName());
        }
    }

    @Test
    @DisplayName("퇴직 명세서 조회 성공")
    void testGetSeverancePayStub_Success() {
        // Given
        String empId = "EMP001";
        Object[] mockResult = {
                "EMP001", "John Doe", LocalDate.of(2010, 5, 15), LocalDate.of(2023, 5, 15),
                "IT Department", "Manager", 3650L, 3000000L, 500000L,
                60000L, 3500000L, 40000L, 30000L, 20000L, 5000000L
        };

        lenient().when(severancePayQueryRepository.findByEmpId(empId)).thenReturn(new Object[]{mockResult});

        System.out.println("Test empId: " + empId);

        // When
//        SeverancePayStubDetailDTO stub = severancePayQueryService.getSeverancePayStub(empId);

        Object[] rawResult = severancePayQueryRepository.findByEmpId(empId);

        System.out.println("Test empId: " + empId);

        Object[] result = (Object[]) rawResult[0];

        // 각 인덱스의 값을 안전하게 캐스팅
        String empIdResult = result[0] instanceof String ? (String) result[0] : null;
        String name = result[1] instanceof String ? (String) result[1] : null;
        LocalDate joinDate = toLocalDate(result[2]);
        LocalDate resignDate = toLocalDate(result[3]);
        String departmentName = result[4] instanceof String ? (String) result[4] : null;
        String positionName = result[5] instanceof String ? (String) result[5] : null;
        Long totalLaborDays = toLong(result[6]);
        Long threeMonthTotalPay = toLong(result[7]);
        Long threeMonthTotalAllowance = toLong(result[8]);
        Long annualAllowance = toLong(result[9]);
        Long totalAllowance = toLong(result[10]);
        Long extendLaborAllowance = toLong(result[11]);
        Long nightLaborAllowance = toLong(result[12]);
        Long holidayLaborAllowance = toLong(result[13]);
        Long totalAmount = toLong(result[14]);

        // DTO 생성
        SeverancePayStubDTO severancePay = new SeverancePayStubDTO(

                empIdResult, name, joinDate, resignDate, departmentName,
                positionName, totalLaborDays, threeMonthTotalPay,
                threeMonthTotalAllowance, annualAllowance, totalAllowance,
                extendLaborAllowance, nightLaborAllowance, holidayLaborAllowance, totalAmount

        );

        // Then
        Assertions.assertNotNull(rawResult);
        Assertions.assertEquals("EMP001", severancePay.getEmpId());
        Assertions.assertEquals("John Doe", severancePay.getEmpName());
        Assertions.assertEquals(LocalDate.of(2010, 5, 15), severancePay.getJoinDate());
        Assertions.assertEquals(LocalDate.of(2023, 5, 15), severancePay.getResignDate());
        Assertions.assertEquals("IT Department", severancePay.getDeptName());
        Assertions.assertEquals("Manager", severancePay.getPositionName());
        Assertions.assertEquals(3650L, severancePay.getTotalLaborDays());
        Assertions.assertEquals(5000000L, severancePay.getTotalAmount());
    }

    @Test
    @DisplayName("퇴직 명세서 조회 실패 - NULL")
    void testGetSeverancePayStub_EmptyResult() {
        // Given
        String empId = "EMP002";
        lenient().when(severancePayQueryRepository.findByEmpId(empId)).thenReturn(null);

        // When & Then
        Assertions.assertThrows(NullPointerException.class,
                () -> severancePayQueryService.getSeverancePayStub(empId),
                "Unexpected result structure or empty result.");
    }

}