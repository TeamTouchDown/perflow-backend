package com.touchdown.perflowbackend.payment.command.domain.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;

public interface InsuranceRateRepository {

    InsuranceRate save(InsuranceRate newInsuranceRate);

}
