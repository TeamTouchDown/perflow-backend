package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.employee.command.domain.aggregate.Employee;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;

import java.util.Optional;

public interface WorkAttitudeAttendanceCommandRepository {

    Attendance save(Attendance attendance);

    boolean existsByEmpIdAndCheckOutDateTimeIsNull(Employee empId);

    Optional<Attendance> findByEmpIdAndCheckOutDateTimeIsNull(Employee empId);
}
