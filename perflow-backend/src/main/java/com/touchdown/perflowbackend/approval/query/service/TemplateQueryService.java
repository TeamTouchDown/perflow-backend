package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.domain.aggregate.Template;
import com.touchdown.perflowbackend.approval.command.mapper.TemplateMapper;
import com.touchdown.perflowbackend.approval.query.dto.TemplateListResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.TemplateResponseDTO;
import com.touchdown.perflowbackend.approval.query.repository.TemplateQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateQueryService {

    private final TemplateQueryRepository templateQueryRepository;

    @Transactional
    public Page<TemplateListResponseDTO> getTemplates(Pageable pageable) {
        // Repository에서 Page<Template> 조회
        Page<Template> templatePage = templateQueryRepository.findAll(pageable);

        // 엔터티 -> DTO 변환
        return templatePage.map(template -> TemplateMapper.toTemplateListResponseDTO(
                List.of(TemplateMapper.toTemplateResponseDTO(template)),
                templatePage.getNumber() + 1,
                templatePage.getTotalPages()
        ));
    }


}
