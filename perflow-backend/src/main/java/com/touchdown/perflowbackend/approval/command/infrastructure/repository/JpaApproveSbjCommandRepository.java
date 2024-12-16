package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.repository.ApproveSbjCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaApproveSbjCommandRepository extends ApproveSbjCommandRepository, JpaRepository<ApproveSbj, Long> {

}
