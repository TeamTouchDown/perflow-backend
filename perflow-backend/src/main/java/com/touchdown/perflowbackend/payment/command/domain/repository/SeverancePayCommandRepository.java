package com.touchdown.perflowbackend.payment.command.domain.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SeverancePayCommandRepository {

    SeverancePay save(SeverancePay severancePay);

    @Query("SELECT s FROM SeverancePay s JOIN FETCH s.severancePayDetailList WHERE s.severancePayId = :severancePayId")
    Optional<SeverancePay> findBySeverancePayIdWithDetails(Long severancePayId);
}
