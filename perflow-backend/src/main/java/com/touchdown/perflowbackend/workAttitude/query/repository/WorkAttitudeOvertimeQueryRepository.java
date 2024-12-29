package com.touchdown.perflowbackend.workAttitude.query.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.query.dto.ThreeMonthOvertimeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface WorkAttitudeOvertimeQueryRepository extends JpaRepository<Overtime, Long> {

    // 1. 삭제되지 않은 모든 초과근무 데이터 조회
    @Query("SELECT o FROM Overtime o WHERE o.status != 'DELETED'")
    List<Overtime> findAllNotDeleted();

    // 2. 특정 사원의 삭제되지 않은 초과근무 데이터 조회
    @Query("SELECT o FROM Overtime o WHERE o.empId.empId = :empId AND o.status != 'DELETED'")
    List<Overtime> findByEmpIdNotDeleted(@Param("empId") String empId);

    // 3. 최근 3개월 내 퇴직 직원들의 초과근무 합산 요약 조회
    @Query(value = "SELECT e.emp_id AS empId, " +
            "SUM(CASE WHEN o.overtime_type = 'NIGHT' THEN TIMESTAMPDIFF(HOUR, o.overtime_start, o.overtime_end) ELSE 0 END) AS nightHours, " +
            "SUM(CASE WHEN o.overtime_type = 'HOLIDAY' THEN TIMESTAMPDIFF(HOUR, o.overtime_start, o.overtime_end) ELSE 0 END) AS holidayHours, " +
            "SUM(CASE WHEN o.overtime_type = 'EXTENDED' THEN TIMESTAMPDIFF(HOUR, o.overtime_start, o.overtime_end) ELSE 0 END) AS extendedHours " +
            "FROM Overtime o " +
            "JOIN Employee e ON e.emp_id = o.emp_id " +
            "WHERE e.resign_date IS NOT NULL AND o.overtime_status = 'CONFIRMED' " +
            "AND DATE(o.update_datetime) BETWEEN :threeMonthsAgo AND e.resign_date " +
            "GROUP BY e.emp_id", nativeQuery = true)
    List<ThreeMonthOvertimeDTO> findOvertimeSummaryForResignedEmployees(@Param("threeMonthsAgo") LocalDate threeMonthsAgo);

    // 4. 팀장의 팀원 초과근무 데이터 조회
    @Query("SELECT o FROM Overtime o " +
            "WHERE o.empId.dept.departmentId = :deptId AND o.status != 'DELETED'")
    List<Overtime> findTeamOvertimes(@Param("deptId") Long deptId);



    // 5. 특정 승인자(결재자)가 승인해야 하는 초과근무 목록 조회
    @Query("SELECT o FROM Overtime o " +
            "WHERE o.approver.empId = :approverId AND o.status != 'DELETED' AND o.overtimeStatus = 'PENDING'")
    List<Overtime> findPendingApprovalByApproverId(@Param("approverId") String approverId);

    @Query(value = "SELECT DATE_FORMAT(o.overtime_start, '%Y-%m') AS month, " +
            "o.overtime_type AS type, " +
            "SUM(TIMESTAMPDIFF(HOUR, o.overtime_start, o.overtime_end)) AS hours " +
            "FROM overtime o " +
            "WHERE o.emp_id = :empId " +
            "AND o.overtime_status = 'CONFIRMED' " + // 승인된 초과근무만 계산
            "GROUP BY month, o.overtime_type " +
            "ORDER BY month ASC", nativeQuery = true)
    List<Object[]> findMonthlyOvertimeSummary(@Param("empId") String empId);


}
