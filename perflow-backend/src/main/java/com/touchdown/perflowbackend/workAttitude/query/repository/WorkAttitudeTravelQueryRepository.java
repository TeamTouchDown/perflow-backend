package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkAttitudeTravelQueryRepository extends JpaRepository<Travel, Long> {

    // 특정 사원이 신청한 출장 중 소프트 삭제 되지 않은 것 조회
    List<Travel> findByEmployee_EmpIdAndStatusNot(String currentEmpId, Status status);

    // 특정 팀장(approver)에게 결재 요청된 출장 중 소프트 삭제 되지 않은 것 조회
    List<Travel> findByApprover_EmpIdAndStatusNot(String currentApproverId, Status status);

    // 특정 팀장(approver)에게 결재 요청된 출장 중, 상태가 PENDING이고 소프트 삭제 되지 않은 것 조회
    List<Travel> findByApprover_EmpIdAndTravelStatusAndStatusNot(String currentApproverId, Status travelStatus, Status status);

    // 모든 출장 중 소프트 삭제 되지 않은 것 조회
    List<Travel> findByStatusNot(Status status);
}
