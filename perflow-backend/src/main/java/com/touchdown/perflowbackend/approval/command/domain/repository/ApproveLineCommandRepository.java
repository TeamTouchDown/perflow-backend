package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApproveLineCommandRepository {

    ApproveLine save(ApproveLine approveLine);

    @Query("SELECT MAX(a.groupId) FROM ApproveLine a")
    Long findMaxGroupId();

    @Query("SELECT line FROM ApproveLine line " +
            "WHERE line.doc.docId = :docId " +
            "AND line.approveLineOrder > :currentOrder " +
            "ORDER BY line.approveLineOrder ASC")
    Optional<ApproveLine> findNextApproveLineAsc(@Param("docId") Long approveLineId, @Param("currentOrder") Long currentOrder);

    @Query("SELECT a FROM ApproveLine a WHERE a.groupId = :groupId")
    Optional<List<ApproveLine>> findAllByGroupId(@Param("groupId") Long groupId);
}
