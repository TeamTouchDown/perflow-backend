package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.OvertimeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkAttributeOvertimeQueryRepository extends JpaRepository<Overtime, Long> {

    // 팀장용: 삭제되지 않은 모든 초과근무 데이터 조회
    @Query("SELECT o FROM Overtime o WHERE o.status != 'DELETED'")
    List<Overtime> findAllNotDeleted();

    // 사원용: 특정 사원의 삭제되지 않은 초과근무 데이터 조회
    @Query("SELECT o FROM Overtime o WHERE o.empId.empId = :empId AND o.status != 'DELETED'")
    List<Overtime> findByEmpIdNotDeleted(@Param("empId") String empId);
}

