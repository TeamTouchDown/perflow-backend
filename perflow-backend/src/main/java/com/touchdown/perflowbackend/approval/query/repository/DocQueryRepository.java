package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocQueryRepository extends JpaRepository<Doc, Long> {

    @Query("SELECT DISTINCT doc FROM Doc doc " +
            "JOIN doc.approveLines line " +
            "JOIN line.approveSbjs sbj " +
            "WHERE sbj.sbjUser.empId = :empId " +
            "AND sbj.status = 'ACTIVATED' " +
            "AND line.status = 'PENDING' " +
            "AND doc.status = 'ACTIVATED' ")
    Page<Doc> findWaitingDocsByUser(@Param("empId") String empId, Pageable pageable);

    @Query("SELECT DISTINCT doc FROM Doc doc " +
            "JOIN doc.approveLines line " +
            "JOIN line.approveSbjs sbj " +
            "WHERE sbj.sbjUser.empId = :empId " +
            "AND ( sbj.status ='APPROVED' OR sbj.status = 'REJECTED') " +
            "AND doc.status <> 'DELETED'")
    Page<Doc> findProcessedDocsByUser(@Param("empId") String empId, Pageable pageable);
}
