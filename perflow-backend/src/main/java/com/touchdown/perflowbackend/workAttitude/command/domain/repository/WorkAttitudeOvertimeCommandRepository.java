package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkAttitudeOvertimeCommandRepository {
    Overtime save(Overtime overtime);

    Optional<Overtime> findById(Long overtimeId);

    @Query("SELECT COUNT(o) > 0 FROM Overtime o " +
            "WHERE o.empId.empId = :empId " +
            "AND o.status = :status " +
            "AND o.overtimeStart <= :endDate " +
            "AND o.overtimeEnd >= :startDate")
    boolean existsByEmpIdAndStatusAndOvertimeStartLessThanEqualAndOvertimeEndGreaterThanEqual(
            @Param("empId") String empId,
            @Param("status") Status status,
            @Param("endDate") LocalDateTime endDate,
            @Param("startDate") LocalDateTime startDate);


}
