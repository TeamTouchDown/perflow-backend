package com.touchdown.perflowbackend.workAttitude.command.infrastructure.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;
import com.touchdown.perflowbackend.workAttitude.command.domain.repository.WorkAttitudeAttendanceCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaWorkAttitudeAttendanceRepository extends WorkAttitudeAttendanceCommandRepository, JpaRepository<Attendance, Long> {
}
