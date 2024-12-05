package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.FieldType;
import com.touchdown.perflowbackend.approval.command.domain.repository.FieldTypeCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFieldTypeCommandRepository extends JpaRepository<FieldType, Long>, FieldTypeCommandRepository {
}
