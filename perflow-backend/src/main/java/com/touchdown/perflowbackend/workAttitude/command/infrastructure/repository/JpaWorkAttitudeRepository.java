package com.touchdown.perflowbackend.workAttitude.command.infrastructure.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Status;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeTravelCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaWorkAttitudeRepository extends WorkAttitudeTravelCommandRepository, JpaRepository<Travel, Long> {

}
