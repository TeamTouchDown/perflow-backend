package com.touchdown.perflowbackend.payment.command.domain.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.Payroll;

import java.util.List;

public interface PayrollCommandRepository {

    void saveAll(List<Payroll> payrollList);

}
