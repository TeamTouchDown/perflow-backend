package com.touchdown.perflowbackend.approval.command.infrastructure.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.command.domain.repository.TemplateCommandRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTemplateCommandRepository extends JpaRepository<Template, Long>, TemplateCommandRepository {
}
