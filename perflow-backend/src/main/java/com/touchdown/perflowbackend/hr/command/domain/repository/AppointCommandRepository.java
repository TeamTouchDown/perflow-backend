package com.touchdown.perflowbackend.hr.command.domain.repository;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Appoint;

public interface AppointCommandRepository {

    Appoint save(Appoint appoint);
}
