package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.query.dto.InboxDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.OutboxDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.ProcessedDocListResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.WaitingDocListResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocQueryRepository extends JpaRepository<Doc, Long>, JpaSpecificationExecutor<Doc> {

    // 대기 문서 목록 조회 시
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

    // 수신함 문서 조회 시
    @Query("SELECT DISTINCT new com.touchdown.perflowbackend.approval.query.dto.InboxDocListResponseDTO(\n" +
            "   doc.docId, " +
            "   doc.title, " +
            "   doc.createUser.name, " +
            "   doc.createDatetime, " +
            "   CASE " +
            "       WHEN doc.status IN (com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.APPROVED, com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.REJECTED) " +
            "           THEN doc.updateDatetime " +
            "       ELSE NULL " +
            "   END, " +
            "   CASE " +
            "       WHEN doc.status = com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.APPROVED THEN '승인' " +
            "       WHEN doc.status = com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.REJECTED THEN '반려' " +
            "       ELSE '진행'\n" +
            "   END," +
            "   sbj.approveLine.approveLineId, " +
            "   sbj.approveSbjId " +
            ") " +
            "FROM Doc doc " +
            "LEFT JOIN doc.approveLines line " +
            "LEFT JOIN line.approveSbjs sbj " +
            "LEFT JOIN doc.shares share " +
            "WHERE ( " +
            "    sbj.sbjUser.empId = :empId " +    // 내가(로그인 한 사원이) 결재선에 포함된 경우
            "    OR share.shareObjUser.empId = :empId " + // 내가 공유 대상에 포함된 경우
            "    OR ( " +
            "        sbj.sbjUser.dept.departmentId = :deptId " +    // 결재선에 포함된 사람의 부서가 나와 같고
            "        AND sbj.sbjUser.position.positionLevel <= :positionLevel" +    // 결재선에 포함된 사람의 직위가 나보다 낮은 경우
            "    ) " +
            ") " +
            "ORDER BY doc.createDatetime DESC")
    Page<InboxDocListResponseDTO> findInboxDocs(Pageable pageable, String empId, Long deptId, Integer positionLevel);

    // 발신함 문서 목록 조회 시
    @Query("SELECT new com.touchdown.perflowbackend.approval.query.dto.OutboxDocListResponseDTO( " +
            "   doc.docId," +
            "   doc.title," +
            "   doc.createDatetime," +
            "   doc.status" +
            ")" +
            "FROM Doc doc " +
            "WHERE doc.createUser.empId = :empId " +
            "AND doc.status <> 'DELETED' " +
            "ORDER BY doc.createDatetime DESC")
    Page<OutboxDocListResponseDTO> findOutBoxDocs(Pageable pageable, String empId);

    // 처리 문서 목록 조회 시
    @Query("SELECT new com.touchdown.perflowbackend.approval.query.dto.ProcessedDocListResponseDTO( " +
            "   doc.docId, " +
            "   doc.title, " +
            "   doc.createUser.name, " +
            "   sbj.sbjUser.empId, " +
            "   sbj.approveLine.approveLineId, " +
            "   sbj.approveSbjId, " +
            "   doc.createDatetime, " +
            "   sbj.completeDatetime " +
            ") " +
            "FROM ApproveSbj sbj " +
            "JOIN sbj.approveLine line " +
            "JOIN line.doc doc " +
            "WHERE sbj.sbjUser.empId = :empId " +
            "AND ( sbj.status = 'APPROVED' OR sbj.status = 'REJECTED') " +
            "AND doc.status <> 'APPROVED' " +
            "ORDER BY sbj.completeDatetime DESC")
    Page<ProcessedDocListResponseDTO> findProcessedDocs(Pageable pageable, String empId);

    // 대기 문서 목록 검색 시
}
