package com.touchdown.perflowbackend.payment.command.domain.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;

import java.util.List;
import java.util.Optional;

public interface PayrollCommandRepository {

    List saveAll(Iterable payrollList);

    Payroll save(Payroll payroll);

    Optional<Payroll> findByPayrollId(Long payrollId);
}
