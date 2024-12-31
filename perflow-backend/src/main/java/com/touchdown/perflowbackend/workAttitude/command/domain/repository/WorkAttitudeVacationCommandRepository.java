package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Vacation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkAttitudeVacationCommandRepository {

    Vacation save(Vacation vacation);

    Optional<Vacation> findById(Long vacationId);


    // 휴가 중복 검증 (날짜 범위 내 활성화 상태)
    @Query("SELECT COUNT(v) > 0 FROM Vacation v " +
            "WHERE v.empId.empId = :empId " +
            "AND v.status = :status " +
            "AND v.vacationStart <= :endDate " +
            "AND v.vacationEnd >= :startDate")
    boolean existsByEmpIdAndStatusAndVacationStartAndVacationEnd(
            @Param("empId") String empId,
            @Param("status") Status status,
            @Param("endDate") LocalDateTime endDate,
            @Param("startDate") LocalDateTime startDate);

}
