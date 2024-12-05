package com.touchdown.perflowbackend.approval.query.repository;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TemplateQueryRepository extends JpaRepository<Template, Long> {

    @Query("SELECT t FROM Template t WHERE t.status <> 'DELETED'")
    Page<Template> findAllTemplates(Pageable pageable);

    @Query("""
            SELECT t FROM Template t
            JOIN FETCH t.fields f
            JOIN FETCH f.fieldType ft
            WHERE t.templateId = :templateId
              AND t.status = 'ACTIVATED'
              AND f.status = 'ACTIVATED'
            """)
    Optional<Template> findOne(@Param("templateId") Long templateId);
}
