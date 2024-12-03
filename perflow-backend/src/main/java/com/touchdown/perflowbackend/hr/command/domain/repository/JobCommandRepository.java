package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Job;

import java.util.Optional;

public interface JobCommandRepository {

    Optional<Job> findById(Long jobId);
}
