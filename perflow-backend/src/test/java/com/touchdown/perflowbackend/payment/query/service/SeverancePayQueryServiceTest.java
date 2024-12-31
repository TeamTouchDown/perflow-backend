package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayDTO;
import com.touchdown.perflowbackend.payment.query.dto.SeverancePayDetailResponseDTO;
import com.touchdown.perflowbackend.payment.query.repository.SeverancePayQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
                Status.PENDING              // severanceStatus
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
                        (Status) result[12]

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
}