package com.touchdown.perflowbackend.payment.command.domain.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.command.domain.aggregate.PayrollDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PayrollCommandRepository {

    List saveAll(Iterable payrollList);

    Payroll save(Payroll payroll);

    PayrollDetail save(PayrollDetail payrollDetail);

    @Query("SELECT p FROM Payroll p JOIN FETCH p.payrollDetailList WHERE p.payrollId = :payrollId")
    Optional<Payroll> findByPayrollIdWithDetails(@Param("payrollId") Long payrollId);
}
