package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ApproveLineCommandRepository {

    ApproveLine save(ApproveLine approveLine);

    Optional<List<ApproveLine>> findByGroupId(Long groupId);
}
