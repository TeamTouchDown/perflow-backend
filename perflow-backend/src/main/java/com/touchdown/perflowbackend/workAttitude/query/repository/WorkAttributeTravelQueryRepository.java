package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WorkAttributeTravelQueryRepository extends JpaRepository<Travel, Long> {

    // 직원용: 삭제되지 않은 내역 조회
    @Query("SELECT t FROM Travel t WHERE t.employee = :employee AND t.status != 'DELETED'")
    List<Travel> findAllByEmployeeAndNotDeleted(@Param("employee") Employee employee);

    // 팀장용: 모든 상태의 내역 조회
    @Query("SELECT t FROM Travel t WHERE t.employee = :employee")
    List<Travel> findAllByEmployee(@Param("employee") Employee employee);
}
