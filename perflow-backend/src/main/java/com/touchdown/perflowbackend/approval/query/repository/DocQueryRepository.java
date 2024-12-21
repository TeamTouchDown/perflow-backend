package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.query.dto.WaitingDocListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocQueryRepository extends JpaRepository<Doc, Long> {

//    @Query("SELECT DISTINCT doc FROM Doc doc " +
//            "JOIN doc.approveLines line " +
//            "JOIN line.approveSbjs sbj " +
//            "WHERE sbj.sbjUser.empId = :empId " +
//            "AND sbj.status = 'ACTIVATED' " +
//            "AND line.status = 'PENDING' " +
//            "AND doc.status = 'ACTIVATED' ")
@Query("SELECT DISTINCT new com.touchdown.perflowbackend.approval.query.dto.WaitingDocListResponseDTO(" +
        "doc.docId, doc.title, doc.createUser.name, doc.createUser.empId, " +
        "sbj.approveLine.approveLineId, sbj.approveSbjId, doc.createDatetime) " +
        "FROM Doc doc " +
        "JOIN doc.approveLines line " +
        "JOIN line.approveSbjs sbj " +
        "WHERE sbj.sbjUser.empId = :empId " +  // 현재 사용자에 해당하는 결재 주체만
        "AND sbj.status = 'ACTIVATED' " +     // 활성화된 결재 주체
        "AND sbj.status IN (com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.ACTIVATED, " +
        "                   com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.PENDING) " +      // 처리 대기 중인 결재선
        "AND doc.status = 'ACTIVATED' " +   // 활성화된 문서
        "AND NOT EXISTS ( " +   // 현재 결재선보다 approveLineOrder 가 낮은 결재선 중 승인되지 않은 상태가 있으면 제외
        "   SELECT 1 FROM ApproveLine prevLine " +
        "   WHERE prevLine.doc = doc " +
        "   AND prevLine.approveLineOrder < line.approveLineOrder " +
        "   AND prevLine.status <> 'APPROVED' " +
        ") " +
        "ORDER BY doc.docId ASC")  // 오래된 순 정렬
    Page<WaitingDocListResponseDTO> findWaitingDocsByUser(@Param("empId") String empId, Pageable pageable);

    @Query("SELECT DISTINCT doc FROM Doc doc " +
            "JOIN doc.approveLines line " +
            "JOIN line.approveSbjs sbj " +
            "WHERE sbj.sbjUser.empId = :empId " +
            "AND ( sbj.status ='APPROVED' OR sbj.status = 'REJECTED') " +
            "AND doc.status <> 'DELETED'")
    Page<Doc> findProcessedDocsByUser(@Param("empId") String empId, Pageable pageable);
}
