package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDocCommandRepository extends JpaRepository<Doc, Long>, DocCommandRepository {
}
