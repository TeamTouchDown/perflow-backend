package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.DocField;

public interface DocFieldCommandRepository {

    void save(DocField build);
}
