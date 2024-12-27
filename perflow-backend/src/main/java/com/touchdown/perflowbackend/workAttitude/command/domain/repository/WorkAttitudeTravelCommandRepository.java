package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;

import java.time.LocalDateTime;
import java.util.Optional;

public interface WorkAttitudeTravelCommandRepository {

    Optional<Travel> findById(Long travelId);
    // Travel 엔터티 저장
    Travel save(Travel travel);
    // Travel 삭제
    void deleteById(Long id);

    boolean existsByEmployee_EmpIdAndStatusNotAndTravelEndGreaterThanEqualAndTravelStartLessThanEqual(
            String empId,
            Status status,
            LocalDateTime start,
            LocalDateTime end
    );
}
