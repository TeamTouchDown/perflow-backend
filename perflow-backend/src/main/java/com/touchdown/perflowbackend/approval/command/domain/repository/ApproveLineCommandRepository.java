package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;

public interface ApproveLineCommandRepository {

    ApproveLine save(ApproveLine approveLine);
}
