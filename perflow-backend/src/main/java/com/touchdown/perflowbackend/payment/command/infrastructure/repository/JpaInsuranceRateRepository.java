package com.touchdown.perflowbackend.payment.command.infrastructure.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;
import com.touchdown.perflowbackend.payment.command.domain.repository.InsuranceRateCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInsuranceRateRepository extends InsuranceRateCommandRepository, JpaRepository<InsuranceRate, Long> {
}
