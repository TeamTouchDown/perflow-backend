package com.touchdown.perflowbackend.hr.command.infrastructure.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;
import com.touchdown.perflowbackend.hr.command.domain.repository.AppointCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAppointCommandRepository extends JpaRepository<Appoint, Long>, AppointCommandRepository {
}
