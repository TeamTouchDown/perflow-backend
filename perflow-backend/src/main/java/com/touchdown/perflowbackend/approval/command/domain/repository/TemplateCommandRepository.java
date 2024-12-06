package com.touchdown.perflowbackend.approval.command.domain.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemplateCommandRepository{

    Template save(Template newTemplate);

    Optional<Template> findById(Long templateId);

    List<Template> findAllByTemplateIdIn(List<Long> templateIds);

}
