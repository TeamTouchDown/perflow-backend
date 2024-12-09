package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;

public interface DocCommandRepository {
    Doc save(Doc doc);
}
