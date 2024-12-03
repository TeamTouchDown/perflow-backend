package com.touchdown.perflowbackend.Approval.query.repository;

import com.touchdown.perflowbackend.Approval.command.domain.aggregate.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaTemplateQueryRepository extends JpaRepository<Template, Long>, TemplateQueryRepository  {
}
