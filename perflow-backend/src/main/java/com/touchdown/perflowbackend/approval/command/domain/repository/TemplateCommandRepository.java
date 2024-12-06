package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;

import java.util.Optional;

public interface TemplateCommandRepository {

    Template save(Template newTemplate);

    Optional<Template> findById(Long templateId);
}
