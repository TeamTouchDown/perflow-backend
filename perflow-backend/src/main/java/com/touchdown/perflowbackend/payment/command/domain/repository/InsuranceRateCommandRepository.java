package com.touchdown.perflowbackend.payment.command.domain.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;

import java.util.Optional;

public interface InsuranceRateCommandRepository {

    InsuranceRate save(InsuranceRate newInsuranceRate);

    Optional<InsuranceRate> findByInsuranceRateId(Long insuranceRateId);
}
