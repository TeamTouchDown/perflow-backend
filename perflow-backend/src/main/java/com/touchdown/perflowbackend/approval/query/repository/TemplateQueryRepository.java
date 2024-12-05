package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.query.dto.TemplateResponseDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateQueryRepository extends JpaRepository<Template, Long> {

}
