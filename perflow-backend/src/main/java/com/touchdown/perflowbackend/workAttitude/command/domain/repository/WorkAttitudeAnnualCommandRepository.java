package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Annual;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkAttitudeAnnualCommandRepository  {

    Optional<Annual> findById(Long annualId);

    Annual save(Annual annual);

    // 날짜 겹침 확인
    boolean existsByEmpId_EmpIdAndStatusAndAnnualStartLessThanEqualAndAnnualEndGreaterThanEqual(
            String empId, Status status, LocalDateTime endDate, LocalDateTime startDate);
    // 연차 개수 확인
    long countByEmpId_EmpIdAndStatus(String empId, Status status);

}
