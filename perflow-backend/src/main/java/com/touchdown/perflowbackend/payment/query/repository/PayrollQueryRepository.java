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

}
