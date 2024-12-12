package com.touchdown.perflowbackend.payment.query.repository;

import com.touchdown.perflowbackend.payment.command.domain.aggregate.SeverancePay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeverancePayQueryRepository extends JpaRepository<SeverancePay, Long> {

    @Query("SELECT s " +
            "FROM SeverancePay s " +
            "JOIN SeverancePayDetail spd ON s.severancePayId = spd.severancePay.severancePayId " +
            "WHERE s.severancePayId = :severancePayId AND spd.emp.status = 'RESIGNED'")
    Optional<SeverancePay> findBySeverancePaysId(Long severancePayId);

    @Query(value = "SELECT " +
            "s.severance_pay_id, " +
            "e.emp_id, " +
            "e.name, " +
            "e.join_date, " +
            "e.resign_date, " +
            "p.name, " +
            "d.name, " +
            "e.pay * 3 AS threeMonthTotalPay, " +
            "DATEDIFF(e.resign_date, DATE_SUB(e.resign_date, INTERVAL 3 MONTH)) + 1 AS threeMonthTotalDays, " +
            "(spd.extend_labor_allowance + spd.night_labor_allowance + spd.holiday_labor_allowance + spd.annual_allowance) AS threeMonthTotalAllowance, " +
            "DATEDIFF(e.resign_date, e.join_date) + 1 AS totalLaborDays, " +
            "spd.total_amount, " +
            "spd.status " +
            "FROM Severance_pay s " +
            "JOIN Severance_pay_detail spd ON s.severance_pay_id = spd.severance_pay_id " +
            "JOIN Employee e ON spd.emp_id = e.emp_id " +
            "JOIN Position p ON e.position_id = p.position_id " +
            "JOIN Department d ON e.dept_id = d.dept_id " +
            "WHERE e.status = 'RESIGNED' AND s.severance_pay_id = :severancePayId",
            nativeQuery = true)
    List<Object[]> findSeverancePayDetails(Long severancePayId);

}
