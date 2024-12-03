package com.touchdown.perflowbackend.hr.command.infrastructure.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;
import com.touchdown.perflowbackend.hr.command.domain.repository.JobCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaJobCommandRepository extends JpaRepository<Job, Long>, JobCommandRepository {
}
