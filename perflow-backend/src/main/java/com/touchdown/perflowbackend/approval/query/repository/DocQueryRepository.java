package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.query.dto.InboxDocListResponseDTO;
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

    // 수신함 문서 조회 시
    @Query("SELECT DISTINCT new com.touchdown.perflowbackend.approval.query.dto.InboxDocListResponseDTO(\n" +
            "        doc.docId,\n" +
            "        doc.title,\n" +
            "        doc.createUser.name,\n" +
            "        doc.createDatetime,\n" +
            "        CASE\n" +
            "            WHEN doc.status IN (com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.APPROVED, com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.REJECTED)\n" +
            "                THEN doc.updateDatetime\n" +
            "            ELSE NULL\n" +
            "        END,\n" +
            "        CASE\n" +
            "            WHEN doc.status = com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.APPROVED THEN '승인'\n" +
            "            WHEN doc.status = com.touchdown.perflowbackend.approval.command.domain.aggregate.Status.REJECTED THEN '반려'\n" +
            "            ELSE '진행'\n" +
            "        END\n" +
            "    )\n" +
            "    FROM Doc doc\n" +
            "    LEFT JOIN doc.approveLines line\n" +
            "    LEFT JOIN line.approveSbjs sbj\n" +
            "    LEFT JOIN doc.shares share\n" +
            "    WHERE (\n" +
            "        sbj.sbjUser.empId = :empId " +    // 내가(로그인 한 사원이) 결재선에 포함된 경우
            "        OR share.shareObjUser.empId = :empId " + // 내가 공유 대상에 포함된 경우
            "        OR (\n" +
            "            sbj.sbjUser.dept.departmentId = :deptId " +    // 결재선에 포함된 사람의 부서가 나와 같고
            "            AND sbj.sbjUser.position.positionLevel <= :positionLevel" +    // 결재선에 포함된 사람의 직위가 나보다 낮은 경우
            "        )" +
            "    )" +
            "    ORDER BY doc.createDatetime DESC")
    Page<InboxDocListResponseDTO> findInboxDocs(Pageable pageable, String empId, Long deptId, Integer positionLevel);
}
