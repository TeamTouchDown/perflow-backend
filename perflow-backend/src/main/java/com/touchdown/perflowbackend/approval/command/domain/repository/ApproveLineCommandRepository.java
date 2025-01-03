package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ApproveLineCommandRepository extends JpaRepository<ApproveLine, Long> {

    ApproveLine save(ApproveLine approveLine);

    @Query("SELECT MAX(a.groupId) FROM ApproveLine a")
    Long findMaxGroupId();

    @Query("SELECT line FROM ApproveLine line " +
            "WHERE line.doc.docId = :docId " +
            "AND line.approveLineOrder > :currentOrder " +
            "ORDER BY line.approveLineOrder ASC")
    List<ApproveLine> findNextApproveLineAsc(@Param("docId") Long approveLineId, @Param("currentOrder") Long currentOrder);

    @Query("SELECT line FROM ApproveLine line WHERE line.groupId = :groupId")
    Optional<List<ApproveLine>> findAllByGroupId(@Param("groupId") Long groupId);

    // 나의 결재선 일괄 삭제
    @Query("SELECT line FROM ApproveLine line " +
            "WHERE line.groupId IN :groupIds " +
            "AND line.approveTemplateType = 'MY_APPROVE_LINE' " +
            "AND line.status <> 'DELETED'")
    List<ApproveLine> findAllByGroupIds(List<Long> groupIds);
}
