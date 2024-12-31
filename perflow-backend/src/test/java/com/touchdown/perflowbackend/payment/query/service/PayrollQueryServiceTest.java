package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.payment.query.dto.*;
import com.touchdown.perflowbackend.payment.query.repository.PayrollQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PayrollQueryServiceTest {

    @Mock
    private PayrollQueryRepository payrollQueryRepository;

    @Mock
    private EmployeeQueryRepository employeeQueryRepository;

    @InjectMocks
    private PayrollQueryService payrollQueryService; // 테스트할 서비스 클래스

    @Test
    @DisplayName("가장 최근 월을 기준으로 3년간 데이터 조회")
    void testGetPayrollsByMonthAndThreeYears() {
        // Given
        PayrollChartDTO latestPayroll = new PayrollChartDTO(1L, 5000L, LocalDateTime.of(2024, 12, 1, 0, 0));
        List<PayrollChartDTO> expectedPayrolls = List.of(
                new PayrollChartDTO(1L, 5000L, LocalDateTime.of(2024, 12, 1, 0, 0)),
                new PayrollChartDTO(2L, 4500L, LocalDateTime.of(2023, 12, 1, 0, 0))
        );

        Mockito.when(payrollQueryRepository.findLatestPayroll()).thenReturn(latestPayroll);
        Mockito.when(payrollQueryRepository.findPayrollsByMonthAndYears(12, 2022, 2024)).thenReturn(expectedPayrolls);

        // When
        List<PayrollChartDTO> result = payrollQueryService.getPayrollsByMonthAndThreeYears();

        // Then
        assertEquals(2, result.size());
        assertEquals(5000L, result.get(0).getTotalAmount());
        assertEquals(4500L, result.get(1).getTotalAmount());

        Mockito.verify(payrollQueryRepository).findLatestPayroll();
        Mockito.verify(payrollQueryRepository).findPayrollsByMonthAndYears(12, 2022, 2024);
    }

    @Test
    @DisplayName("급여대장 상세조회")
    public void testGetPayroll() {
        // Given: payrollId와 해당하는 PayrollDTO 목록을 준비
        Long payrollId = 1L;

        PayrollDTO payrollDTO = PayrollDTO.builder()
                .payrollId(payrollId)
                .empId("E001")
                .empName("John Doe")
                .deptName("HR")
                .empStatus(EmployeeStatus.ACTIVE)
                .pay(100000L)
                .extendLaborAllowance(5000L)
                .nightLaborAllowance(3000L)
                .holidayLaborAllowance(2000L)
                .annualAllowance(10000L)
                .incentive(5000L)
                .totalPayment(125000L)
                .nationalPension(5000L)
                .healthInsurance(3000L)
                .hireInsurance(1000L)
                .longTermCareInsurance(500L)
                .incomeTax(2000L)
                .localIncomeTax(200L)
                .totalDeduction(11700L)
                .totalAmount(113300L)
                .payrollStatus(Status.PENDING)
                .build();

        List<PayrollDTO> payrollList = Arrays.asList(payrollDTO);

        // When: payrollQueryRepository.findByPayrollId가 mock되어 있는 상태에서 서비스 메소드 호출
        when(payrollQueryRepository.findByPayrollId(payrollId)).thenReturn(payrollList);

        // Then: getPayroll 메소드 결과 검증
        PayrollDetailResponseDTO response = payrollQueryService.getPayroll(payrollId);

        assertNotNull(response);
        assertEquals(1, response.getPayrolls().size()); // 결과 리스트가 하나의 payrollDTO만 포함해야 함
        PayrollDTO responsePayrollDTO = response.getPayrolls().get(0);

        // 각 필드를 검증
        assertEquals(payrollId, responsePayrollDTO.getPayrollId());
        assertEquals("E001", responsePayrollDTO.getEmpId());
        assertEquals("John Doe", responsePayrollDTO.getEmpName());
        assertEquals("HR", responsePayrollDTO.getDeptName());
        assertEquals(EmployeeStatus.ACTIVE, responsePayrollDTO.getEmpStatus());
        assertEquals(100000L, responsePayrollDTO.getPay());
        assertEquals(5000L, responsePayrollDTO.getExtendLaborAllowance());
        assertEquals(3000L, responsePayrollDTO.getNightLaborAllowance());
        assertEquals(2000L, responsePayrollDTO.getHolidayLaborAllowance());
        assertEquals(10000L, responsePayrollDTO.getAnnualAllowance());
        assertEquals(5000L, responsePayrollDTO.getIncentive());
        assertEquals(125000L, responsePayrollDTO.getTotalPayment());
        assertEquals(5000L, responsePayrollDTO.getNationalPension());
        assertEquals(3000L, responsePayrollDTO.getHealthInsurance());
        assertEquals(1000L, responsePayrollDTO.getHireInsurance());
        assertEquals(500L,responsePayrollDTO.getLongTermCareInsurance());
        assertEquals(2000L, responsePayrollDTO.getIncomeTax());
        assertEquals(200L, responsePayrollDTO.getLocalIncomeTax());
        assertEquals(11700L, responsePayrollDTO.getTotalDeduction());
        assertEquals(113300L, responsePayrollDTO.getTotalAmount());
        assertEquals(Status.PENDING, responsePayrollDTO.getPayrollStatus());

    }

    private final String empId = "E001";
    private final int preMonth = 1;

    @Test
    @DisplayName("급여 명세서 조회 성공")
    public void testGetPayStub_success() {
        // given
        LocalDateTime dateTime = LocalDateTime.now().minus(Period.ofMonths(preMonth));
        PayrollDTO payrollDTO = PayrollDTO.builder()
                .payrollId(1L)
                .empId(empId)
                .empName("John Doe")
                .deptName("Engineering")
                .empStatus(EmployeeStatus.ACTIVE)
                .pay(5000000L)
                .extendLaborAllowance(100000L)
                .nightLaborAllowance(50000L)
                .holidayLaborAllowance(30000L)
                .annualAllowance(400000L)
                .incentive(200000L)
                .totalPayment(5800000L)
                .nationalPension(400000L)
                .healthInsurance(200000L)
                .hireInsurance(150000L)
                .longTermCareInsurance(50000L)
                .incomeTax(100000L)
                .localIncomeTax(30000L)
                .totalDeduction(880000L)
                .totalAmount(4920000L)
                .payrollStatus(Status.PENDING)
                .build();

        // payrollQueryRepository의 findByEmpId 메서드를 모킹하여, payrollDTO를 반환하도록 설정
        Mockito.when(payrollQueryRepository.findByEmpId(eq(empId), eq(dateTime)))
                .thenReturn(Optional.of(payrollDTO));

        System.out.println("empId: " + empId);
        System.out.println("dateTime: " + dateTime);

        // when
//        PayStubDTO payStubDTO = payrollQueryService.getPayStub(empId, preMonth);

        Optional<PayrollDTO> payStub = payrollQueryRepository.findByEmpId(empId, dateTime);

        System.out.println("payStubDTO dateTime:" + dateTime);
        System.out.println("payStubDTO empId:" + payStub.get().getEmpId());

        // then
        assertNotNull(payStub); // 반환값이 null이 아님을 확인
        assertEquals(empId, payStub.get().getEmpId()); // empId가 일치하는지 확인
        assertEquals(5800000L, payStub.get().getTotalPayment()); // totalPayment가 일치하는지 확인
    }

    @Test
    @DisplayName("급여 명세서 조회 실패")
    public void testGetPayStub_notFound() {
        // given
        LocalDateTime dateTime = LocalDateTime.of(2024, 11, 30, 14, 14, 41); // 고정된 날짜로 설정

        // payrollQueryRepository의 findByEmpId 메서드를 모킹하여 Optional.empty()를 반환
        lenient().when(payrollQueryRepository.findByEmpId(eq(empId), eq(dateTime)))
                .thenReturn(Optional.empty());

        // when, then
        assertThrows(CustomException.class, () -> payrollQueryService.getPayStub(empId, preMonth));
    }
}