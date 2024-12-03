package com.touchdown.perflowbackend.Approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.Approval.command.domain.aggregate.TemplateField;
import com.touchdown.perflowbackend.Approval.command.domain.repository.TemplateFieldCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTemplateFieldCommandRepository extends JpaRepository<TemplateField, Long>, TemplateFieldCommandRepository {
}
