package com.touchdown.perflowbackend.payment.query.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SeverancePayQueryRepository extends JpaRepository<SeverancePay, Long> {

    @Query("SELECT s " +
            "FROM SeverancePay s " +
            "JOIN SeverancePayDetail spd ON s.severancePayId = spd.severancePay.severancePayId " +
            "WHERE s.severancePayId = :severancePayId AND spd.emp.status = 'RESIGNED'")
    Optional<SeverancePay> findBySeverancePaysId(Long severancePayId);
}
