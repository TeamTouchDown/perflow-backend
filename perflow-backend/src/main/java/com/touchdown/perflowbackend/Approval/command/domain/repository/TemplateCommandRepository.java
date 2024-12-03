package com.touchdown.perflowbackend.Approval.command.domain.repository;

import com.touchdown.perflowbackend.Approval.command.domain.aggregate.Template;

import java.util.Optional;

public interface TemplateCommandRepository {

    Template save(Template newTemplate);
}
