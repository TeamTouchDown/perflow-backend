package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.TemplateField;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateFieldCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTemplateFieldCommandRepository extends JpaRepository<TemplateField, Long>, TemplateFieldCommandRepository {
}
