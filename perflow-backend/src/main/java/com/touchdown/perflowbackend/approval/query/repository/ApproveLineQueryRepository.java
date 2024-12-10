package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ApproveLineQueryRepository extends JpaRepository<ApproveLine, Long> {

    @Query("SELECT a FROM ApproveLine a " +
            "WHERE a.status <> 'DELETED' " +
            "AND a.createUser.empId = :createUserId " +
            "AND a.approveTemplateType = com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveTemplateType.MANUAL " +
            "AND (:name IS NULL OR a.name LIKE CONCAT('%', :name, '%')) " +
            "AND (:startDate IS NULL OR a.createDatetime >= :startDate) " +
            "AND (:endDate IS NULL OR a.createDatetime <= :endDate)"
        )
    Page<ApproveLine> findAllMyApproveLines(
            Pageable pageable,
            @Param("createUserId") String createUserId,
            @Param("name") String name,
            @Param("startDate")LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
        );


}
