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
        "AND line.status = 'PENDING' " +      // 처리 대기 중인 결재선
        "AND doc.status = 'ACTIVATED' " +     // 활성화된 문서
        "ORDER BY line.approveLineOrder ASC")
    Page<WaitingDocListResponseDTO> findWaitingDocsByUser(@Param("empId") String empId, Pageable pageable);

    @Query("SELECT DISTINCT doc FROM Doc doc " +
            "JOIN doc.approveLines line " +
            "JOIN line.approveSbjs sbj " +
            "WHERE sbj.sbjUser.empId = :empId " +
            "AND ( sbj.status ='APPROVED' OR sbj.status = 'REJECTED') " +
            "AND doc.status <> 'DELETED'")
    Page<Doc> findProcessedDocsByUser(@Param("empId") String empId, Pageable pageable);
}
