package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;

import java.util.List;
import java.util.Optional;

public interface WorkAttitudeTravelCommandRepository {
    // Travel 엔터티 저장
    Travel save(Travel travel);
    // ID로 Travel 조회
    Optional<Travel> findById(Long id);
    // 특정 상태의 Travel 조회
    List<Travel> findAllByTravelStatus(Status travelStatus);
    // 특정 사원의 Travel 조회
    List<Travel> findAllByEmpId(Employee empId);
    // Travel 삭제
    void deleteById(Long id);
}
