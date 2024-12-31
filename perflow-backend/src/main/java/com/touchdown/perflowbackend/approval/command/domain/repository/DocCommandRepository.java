package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;

import java.util.Optional;

public interface DocCommandRepository {

    Doc save(Doc doc);

    Optional<Doc> findById(Long docId);
}
