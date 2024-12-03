package com.touchdown.perflowbackend.Approval.query.repository;

import com.touchdown.perflowbackend.Approval.query.dto.TemplateQueryResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TemplateQueryRepository {

    Page<TemplateQueryResponseDTO> findAll(Pageable pageable);
}
