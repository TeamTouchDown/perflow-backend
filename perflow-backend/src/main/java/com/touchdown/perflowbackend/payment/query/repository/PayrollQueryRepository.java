package com.touchdown.perflowbackend.payment.query.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.query.dto.PayrollChartDTO;
import com.touchdown.perflowbackend.payment.query.dto.PayrollDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PayrollQueryRepository extends JpaRepository<Payroll, Long> {

    @Query("SELECT p FROM Payroll p WHERE p.payrollId = :payrollId")
    Optional<Payroll> findByPayrollsId(Long payrollId);

    @Query("SELECT new com.touchdown.perflowbackend.payment.query.dto.PayrollDTO(" +
            "p.payrollId, pd.emp.empId, pd.emp.name, pd.emp.dept.name, pd.emp.status, " +
            "pd.emp.pay, pd.extendLaborAllowance, pd.nightLaborAllowance, pd.holidayLaborAllowance, pd.annualAllowance, pd.incentive, " +
            "(pd.emp.pay + pd.extendLaborAllowance + pd.nightLaborAllowance + pd.holidayLaborAllowance + pd.annualAllowance + pd.incentive) AS totalPayment," +
            "pd.nationalPension, pd.healthInsurance, pd.hireInsurance, pd.longTermCareInsurance, pd.incomeTax, pd.localIncomeTax, " +
            "(pd.nationalPension + pd.healthInsurance + pd.hireInsurance + pd.longTermCareInsurance + pd.incomeTax + pd.localIncomeTax) AS totalDeduction, " +
            "pd.totalAmount, pd.status) " +
            "FROM Payroll p " +
            "JOIN PayrollDetail pd ON pd.payroll.payrollId = p.payrollId " +
            "WHERE pd.emp.status <> 'RESIGNED' AND p.payrollId = :payrollId")
    List<PayrollDTO> findByPayrollId(Long payrollId);

    // 가장 최근 급여 대장을 조회하는 메서드
    @Query("SELECT new com.touchdown.perflowbackend.payment.query.dto.PayrollChartDTO(" +
            "p.payrollId, SUM(pd.totalAmount), p.createDatetime) " +
            "FROM Payroll p " +
            "JOIN PayrollDetail pd ON p.payrollId = pd.payroll.payrollId " +
            "WHERE p.payrollId = (SELECT MAX(payrollId) FROM Payroll)")
    PayrollChartDTO findLatestPayroll();

    // 특정 월을 기준으로 3년간 급여 데이터 조회
    @Query("SELECT new com.touchdown.perflowbackend.payment.query.dto.PayrollChartDTO(" +
            "p.payrollId, SUM(pd.totalAmount), p.createDatetime) " +
            "FROM Payroll p " +
            "JOIN PayrollDetail pd ON p.payrollId = pd.payroll.payrollId " +
            "WHERE EXTRACT(MONTH FROM p.createDatetime) = :month AND EXTRACT(YEAR FROM p.createDatetime) BETWEEN :startYear AND :endYear " +
            "GROUP BY p.payrollId")
    List<PayrollChartDTO> findPayrollsByMonthAndYears(@Param("month") int month, @Param("startYear") int startYear, @Param("endYear") int endYear);

    // 월 범위와 연도를 기준으로 급여 데이터를 조회 (3개월 데이터)
    @Query("SELECT new com.touchdown.perflowbackend.payment.query.dto.PayrollChartDTO(" +
            "p.payrollId, SUM(pd.totalAmount), p.createDatetime) " +
            "FROM Payroll p " +
            "JOIN PayrollDetail pd ON p.payrollId = pd.payroll.payrollId " +
            "WHERE MONTH(p.createDatetime) BETWEEN :startMonth AND :latestMonth AND YEAR(p.createDatetime) = :year " +
            "GROUP BY p.payrollId")
    List<PayrollChartDTO> findPayrollsByMonths(@Param("startMonth") int startMonth, @Param("latestMonth") int latestMonth, @Param("year") int year);

    // 연도 범위에 해당하는 급여 데이터를 조회 (3년 데이터)
    @Query("SELECT new com.touchdown.perflowbackend.payment.query.dto.PayrollChartDTO(" +
            "p.payrollId, SUM(pd.totalAmount), p.createDatetime) " +
            "FROM Payroll p " +
            "JOIN PayrollDetail pd ON p.payrollId = pd.payroll.payrollId " +
            "WHERE YEAR(p.createDatetime) BETWEEN :startYear AND :latestYear " +
            "GROUP BY p.payrollId")
    List<PayrollChartDTO> findPayrollsByYears(@Param("startYear") int startYear, @Param("latestYear") int latestYear);

    @Query("SELECT new com.touchdown.perflowbackend.payment.query.dto.PayrollDTO(" +
            "p.payrollId, pd.emp.empId, pd.emp.name, pd.emp.dept.name, pd.emp.status, " +
            "pd.emp.pay, pd.extendLaborAllowance, pd.nightLaborAllowance, pd.holidayLaborAllowance, pd.annualAllowance, pd.incentive, " +
            "(pd.emp.pay + pd.extendLaborAllowance + pd.nightLaborAllowance + pd.holidayLaborAllowance + pd.annualAllowance + pd.incentive) AS totalPayment," +
            "pd.nationalPension, pd.healthInsurance, pd.hireInsurance, pd.longTermCareInsurance, pd.incomeTax, pd.localIncomeTax, " +
            "(pd.nationalPension + pd.healthInsurance + pd.hireInsurance + pd.longTermCareInsurance + pd.incomeTax + pd.localIncomeTax) AS totalDeduction, " +
            "pd.totalAmount, pd.status) " +
            "FROM Payroll p " +
            "JOIN PayrollDetail pd ON pd.payroll.payrollId = p.payrollId " +
            "WHERE pd.emp.status <> 'RESIGNED' AND pd.emp.empId = :empId " +
            "AND FUNCTION('YEAR', p.createDatetime) = FUNCTION('YEAR', CURRENT_DATE) " +
            "AND FUNCTION('MONTH', p.createDatetime) = FUNCTION('MONTH', CURRENT_DATE)")
    Optional<PayrollDTO> findByEmpId(String empId);

}
