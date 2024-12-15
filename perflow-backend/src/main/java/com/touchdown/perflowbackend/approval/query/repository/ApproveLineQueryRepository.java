package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.ApproveLine;
import com.touchdown.perflowbackend.approval.query.dto.MyApproveLineGroupResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApproveLineQueryRepository extends JpaRepository<ApproveLine, Long> {

    // 나의 결재선 목록 조회
    @Query("SELECT new com.touchdown.perflowbackend.approval.query.dto.MyApproveLineGroupResponseDTO( " +
            "line.groupId, line.name, line.description, line.createDatetime) " +
            "FROM ApproveLine line " +
            "WHERE line.createUser.empId = :createUserId " +
            "AND line.approveTemplateType = 'MY_APPROVE_LINE'")
    Page<MyApproveLineGroupResponseDTO> findAllMyApproveLines(
            Pageable pageable,
            @Param("createUserId") String createUserId);

    // 나의 결재선 목록 조회 시 결재선(부서, 사원, 결재방식) 상세 정보 조회
    @Query("SELECT line FROM ApproveLine line " +
            "WHERE line.groupId = :groupId " +
            "AND line.approveTemplateType = 'MY_APPROVE_LINE' " +
            "AND line.status <> 'DELETED'")
    List<ApproveLine> findByGroupId(@Param("groupId") Long groupId);
}
