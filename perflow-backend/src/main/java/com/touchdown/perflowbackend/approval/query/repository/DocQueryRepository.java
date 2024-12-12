package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Doc;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocQueryRepository extends JpaRepository<Doc, Long> {
}
