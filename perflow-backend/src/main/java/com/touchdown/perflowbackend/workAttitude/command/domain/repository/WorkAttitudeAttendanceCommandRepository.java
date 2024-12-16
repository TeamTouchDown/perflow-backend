package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Attendance;

public interface WorkAttitudeAttendanceCommandRepository {

    Attendance save(Attendance attendance);
}
