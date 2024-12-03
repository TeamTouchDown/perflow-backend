package com.touchdown.perflowbackend.Approval.command.domain.repository;

import com.touchdown.perflowbackend.Approval.command.domain.aggregate.TemplateField;
import org.springframework.data.jpa.repository.JpaRepository;

// saveAll 사용을 위해 JpaRepository 상속 (saveAll 은 명시적으로 정의 x)
public interface TemplateFieldCommandRepository extends JpaRepository<TemplateField, Long> {

}

