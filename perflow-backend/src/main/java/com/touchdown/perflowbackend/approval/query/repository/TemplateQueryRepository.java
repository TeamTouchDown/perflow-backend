package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemplateQueryRepository extends JpaRepository<Template, Long> {

}
