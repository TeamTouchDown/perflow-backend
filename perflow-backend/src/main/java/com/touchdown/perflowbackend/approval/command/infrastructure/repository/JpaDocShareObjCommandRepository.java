package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.DocShareObj;
import com.touchdown.perflowbackend.approval.command.domain.repository.DocShareObjCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaDocShareObjCommandRepository extends JpaRepository<DocShareObj, Long>, DocShareObjCommandRepository {
}
