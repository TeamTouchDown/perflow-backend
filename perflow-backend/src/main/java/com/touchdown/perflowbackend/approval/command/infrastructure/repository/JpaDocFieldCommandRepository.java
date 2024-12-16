package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.DocField;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocFieldCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDocFieldCommandRepository extends JpaRepository<DocField, Long>, DocFieldCommandRepository {
}
