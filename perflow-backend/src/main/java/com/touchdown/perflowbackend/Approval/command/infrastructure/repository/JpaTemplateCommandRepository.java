package com.touchdown.perflowbackend.Approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.Approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.Approval.command.domain.repository.TemplateCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTemplateCommandRepository extends JpaRepository<Template, Long>, TemplateCommandRepository {
}
