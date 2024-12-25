package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.query.dto.ThreeMonthOvertimeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkAttitudeOvertimeQueryRepository extends JpaRepository<Overtime, Long> {

    // 팀장용: 삭제되지 않은 모든 초과근무 데이터 조회
    @Query("SELECT o FROM Overtime o WHERE o.status != 'DELETED'")
    List<Overtime> findAllNotDeleted();

    // 사원용: 특정 사원의 삭제되지 않은 초과근무 데이터 조회
    @Query("SELECT o FROM Overtime o WHERE o.empId.empId = :empId AND o.status != 'DELETED'")
    List<Overtime> findByEmpIdNotDeleted(@Param("empId") String empId);

    @Query(value = "SELECT e.emp_id AS empId, " +
            "SUM(CASE WHEN o.overtime_type = 'NIGHT' THEN TIMESTAMPDIFF(HOUR, o.overtime_start, o.overtime_end) ELSE 0 END) AS nightHours, " +
            "SUM(CASE WHEN o.overtime_type = 'HOLIDAY' THEN TIMESTAMPDIFF(HOUR, o.overtime_start, o.overtime_end) ELSE 0 END) AS holidayHours, " +
            "SUM(CASE WHEN o.overtime_type = 'EXTENDED' THEN TIMESTAMPDIFF(HOUR, o.overtime_start, o.overtime_end) ELSE 0 END) AS extendedHours " +
            "FROM Overtime o " +
            "JOIN Employee e ON e.emp_id = o.emp_id " +
            "WHERE e.resign_date IS NOT NULL AND o.overtime_status = 'CONFIRMED'" +
            "AND DATE(o.update_datetime) BETWEEN :threeMonthsAgo AND e.resign_date " +
            "GROUP BY e.emp_id", nativeQuery = true)
    List<ThreeMonthOvertimeDTO> findOvertimeSummaryForResignedEmployees(@Param("threeMonthsAgo") LocalDate threeMonthsAgo);
}

