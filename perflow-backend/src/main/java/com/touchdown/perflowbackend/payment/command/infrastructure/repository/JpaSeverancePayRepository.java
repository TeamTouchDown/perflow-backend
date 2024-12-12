package com.touchdown.perflowbackend.payment.command.infrastructure.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import com.touchdown.perflowbackend.payment.command.domain.repository.SeverancePayCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSeverancePayRepository extends JpaRepository<SeverancePay, Long>, SeverancePayCommandRepository {
}
