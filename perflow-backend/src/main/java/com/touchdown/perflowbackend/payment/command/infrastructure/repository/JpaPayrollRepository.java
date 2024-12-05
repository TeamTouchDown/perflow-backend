package com.touchdown.perflowbackend.payment.command.infrastructure.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;
import com.touchdown.perflowbackend.payment.command.domain.repository.PayrollCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPayrollRepository extends PayrollCommandRepository, JpaRepository<Payroll, Long> {
}
