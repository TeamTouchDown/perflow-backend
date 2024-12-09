package com.touchdown.perflowbackend.workAttitude.command.domain.repository;

import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Overtime;
import com.touchdown.perflowbackend.workAttitude.command.domain.aggregate.Travel;

public interface WorkAttributeOvertimeCommandRepository {
    Overtime save(Overtime overtime);
}
