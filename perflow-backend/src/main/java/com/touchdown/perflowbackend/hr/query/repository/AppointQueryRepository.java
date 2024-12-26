package com.touchdown.perflowbackend.hr.query.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointQueryRepository extends JpaRepository<Appoint, Long> {
}
