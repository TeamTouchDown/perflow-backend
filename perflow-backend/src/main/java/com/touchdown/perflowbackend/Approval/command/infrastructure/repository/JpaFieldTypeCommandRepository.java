package com.touchdown.perflowbackend.Approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.Approval.command.domain.aggregate.FieldType;
import com.touchdown.perflowbackend.Approval.command.domain.repository.FieldTypeCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFieldTypeCommandRepository extends JpaRepository<FieldType, Long>, FieldTypeCommandRepository {
}
