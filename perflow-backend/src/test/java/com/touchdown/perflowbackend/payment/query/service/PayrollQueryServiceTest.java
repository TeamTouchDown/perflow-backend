package com.touchdown.perflowbackend.payment.query.service;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.EmployeeStatus;
import com.touchdown.perflowbackend.employee.query.repository.EmployeeQueryRepository;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.payment.query.dto.PayrollDTO;
import com.touchdown.perflowbackend.payment.query.dto.PayrollDetailResponseDTO;
import com.touchdown.perflowbackend.payment.query.repository.PayrollQueryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
}