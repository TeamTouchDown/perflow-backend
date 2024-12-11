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
    @Query("SELECT new com.touchdown.perflowbackend.approval.query.dto.MyApproveLineGroupResponseDTO(" +
            "a.groupId, " +
            "MAX(a.name)," +
            "MAX(a.description)," +
            "MAX(a.createDatetime))" +
            "FROM ApproveLine a " +
            "WHERE a.createUser.empId = :createUserId " +
            "AND a.approveTemplateType = 'MY_APPROVE_LINE' " +
            "AND a.status <> 'DELETED' " +
            "GROUP BY a.groupId")
    Page<MyApproveLineGroupResponseDTO> findAllMyApproveLines(
            Pageable pageable,
            @Param("createUserId") String createUserId);

    // 나의 결재선 목록 조회 시 결재선(부서, 사원, 결재방식) 상세 정보 조회
    @Query("SELECT a FROM ApproveLine a WHERE a.groupId = :groupId AND a.status <> 'DELETED'")
    List<ApproveLine> findByGroupId(@Param("groupId") Long groupId);
}
