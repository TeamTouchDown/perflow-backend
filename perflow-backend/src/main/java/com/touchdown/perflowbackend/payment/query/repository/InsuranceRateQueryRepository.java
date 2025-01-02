package com.touchdown.perflowbackend.payment.query.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.InsuranceRate;
import com.touchdown.perflowbackend.payment.query.dto.InsuranceRateResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InsuranceRateQueryRepository extends JpaRepository<InsuranceRate, Long> {

    @Query("SELECT i FROM InsuranceRate i WHERE i.insuranceRateId = :insuranceRateId")
    Optional<InsuranceRate> findByInsuranceRateId(Long insuranceRateId);

}
