package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApproveLineQueryRepository extends JpaRepository<ApproveLine, Long> {

    @Query("SELECT a FROM ApproveLine a WHERE a.status <> 'DELETED' AND a.createUser.empId = :createUserId AND a.approveTemplateType = 'MY_APPROVE_LINE'")
    Page<ApproveLine> findAllMyApproveLines(Pageable pageable, @Param("createUserId") String createUserId);


}
