package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.SbjType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApproveSbjCommandRepository {

    Optional<ApproveSbj> findById(Long approveSbjId);

    @Query("SELECT sbj FROM ApproveSbj sbj " +
            "JOIN sbj.approveLine line " +
            "JOIN line.doc doc " +
            "WHERE doc.docId = :docId AND sbj.sbjType = :sbjType " +
            "AND (sbj.sbjUser.empId = :approveSbjId OR CAST(sbj.dept.departmentId AS string) = :approveSbjId)")
    Optional<ApproveSbj> findByDocIdAndApproveSbjIdAndType(
            @Param("docId") Long docId,
            @Param("approveSbjId") String approveSbjId,
            @Param("sbjType") SbjType sbjType
        );

//    void save(ApproveSbj approveSbj);

    // 같은 결재선의 모든 결재 주체 조회
//    @Query("SELECT sbj FROM ApproveSbj sbj " +
//            "WHERE sbj.approveLine.approveLineId = :approveLineId")
//    List<ApproveSbj> findApproveSbjsByLineId(@Param("approveLineId") Long approveLineId);
}
