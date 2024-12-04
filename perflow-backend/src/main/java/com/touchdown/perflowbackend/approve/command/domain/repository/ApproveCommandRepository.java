package com.touchdown.perflowbackend.approve.command.domain.repository;

import com.touchdown.perflowbackend.approve.command.domain.aggregate.ApproveSbj;

import java.util.Optional;

public interface ApproveCommandRepository {
    Optional<ApproveSbj> findById(Long approveSbjId);
}
