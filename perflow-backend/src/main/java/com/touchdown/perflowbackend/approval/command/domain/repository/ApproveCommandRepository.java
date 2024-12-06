package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;

import java.util.Optional;

public interface ApproveCommandRepository {

    Optional<ApproveSbj> findById(Long approveSbjId);
}
