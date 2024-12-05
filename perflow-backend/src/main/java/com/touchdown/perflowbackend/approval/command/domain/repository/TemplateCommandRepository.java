package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;

public interface TemplateCommandRepository {

    Template save(Template newTemplate);
}
