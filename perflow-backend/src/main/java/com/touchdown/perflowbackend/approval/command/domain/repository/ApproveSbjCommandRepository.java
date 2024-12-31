package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveSbj;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.EmpDeptType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ApproveSbjCommandRepository {

    Optional<ApproveSbj> findById(Long approveSbjId);

    // todo: DISTINCT 추가 해도 어떤 경우에는 자꾸 2개 이상의 결과를 반환해서 문서 승인을 못하는 경우가 생김
    @Query("SELECT DISTINCT sbj FROM ApproveSbj sbj " +
            "JOIN sbj.approveLine line " +
            "JOIN line.doc doc " +
            "WHERE doc.docId = :docId " +
            "AND line.approveLineId = :approveLineId " +
            "AND sbj.approveSbjId = :approveSbjId " +
            "AND sbj.empDeptType = :sbjType ")
    Optional<ApproveSbj> findByDocIdAndApproveSbjIdAndType(
            @Param("docId") Long docId,
            @Param("sbjType") EmpDeptType empDeptType,
            @Param("approveLineId") Long approveLineId,
            @Param("approveSbjId") Long approveSbjId
        );

    //ApproveSbj save(ApproveSbj approveSbj);
//        @Query("SELECT sbj FROM ApproveSbj sbj " +
//            "JOIN sbj.approveLine line " +
//            "JOIN line.doc doc " +
//            "WHERE doc.docId = :docId AND sbj.empDeptType = :sbjType " +
//            "AND (sbj.sbjUser.empId = :approveSbjId OR CAST(sbj.dept.departmentId AS string) = :approveSbjId)")
//    Optional<ApproveSbj> findByDocIdAndApproveSbjIdAndType(
//            @Param("docId") Long docId,
//            @Param("approveSbjId") String approveSbjId,
//            @Param("sbjType") EmpDeptType empDeptType
//        );

//    void save(ApproveSbj approveSbj);

    // 같은 결재선의 모든 결재 주체 조회
//    @Query("SELECT sbj FROM ApproveSbj sbj " +
//            "WHERE sbj.approveLine.approveLineId = :approveLineId")
//    List<ApproveSbj> findApproveSbjsByLineId(@Param("approveLineId") Long approveLineId);
}
