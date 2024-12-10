package com.touchdown.perflowbackend.payment.query.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.query.dto.PayrollDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PayrollQueryRepository extends JpaRepository<Payroll, Long> {

    @Query("SELECT p FROM Payroll p WHERE p.payrollId = :payrollId")
    Optional<Payroll> findByPayrollsId(Long payrollId);

    @Query("SELECT new com.touchdown.perflowbackend.payment.query.dto.PayrollDTO(" +
            "p.payrollId, e.empId, e.name, e.dept.name, e.status, " +
            "e.pay, pd.extendLaborAllowance, pd.nightLaborAllowance, pd.holidayLaborAllowance, pd.annualAllowance, pd.incentive, " +
            "(e.pay + pd.extendLaborAllowance + pd.nightLaborAllowance + pd.holidayLaborAllowance + pd.annualAllowance + pd.incentive) AS totalPayment," +
            "pd.nationalPension, pd.healthInsurance, pd.hireInsurance, pd.longTermCareInsurance, pd.incomeTax, pd.localIncomeTax, " +
            "(pd.nationalPension + pd.healthInsurance + pd.hireInsurance + pd.longTermCareInsurance + pd.incomeTax + pd.localIncomeTax) AS totalDeduction, " +
            "pd.totalAmount, pd.status) " +
            "FROM Payroll p " +
            "JOIN PayrollDetail pd ON pd.payroll.payrollId = p.payrollId " +
            "JOIN Employee e ON pd.emp.empId = e.empId " +
            "WHERE e.status <> 'RESIGNED' AND p.payrollId = :payrollId")
    List<PayrollDTO> findByPayrollId(Long payrollId);

}
