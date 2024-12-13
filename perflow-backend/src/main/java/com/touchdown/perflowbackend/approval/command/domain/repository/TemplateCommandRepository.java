package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.command.domain.aggregate.TemplateField;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemplateCommandRepository{

    Template save(Template newTemplate);

    Optional<Template> findById(Long templateId);

    List<Template> findAllByTemplateIdIn(List<Long> templateIds);

}
