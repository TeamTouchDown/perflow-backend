package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.DocField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocFieldQueryRepository extends JpaRepository<DocField, Long> {
}
