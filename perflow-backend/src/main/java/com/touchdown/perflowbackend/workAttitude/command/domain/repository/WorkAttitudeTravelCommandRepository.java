package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;

import java.util.List;
import java.util.Optional;

public interface WorkAttitudeTravelCommandRepository {
    // Travel 엔터티 저장
    Travel save(Travel travel);
    // Travel 삭제
    void deleteById(Long id);
}
