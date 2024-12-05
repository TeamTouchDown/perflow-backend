package com.touchdown.perflowbackend.approval.query.service;

import com.touchdown.perflowbackend.approval.command.mapper.TemplateMapper;
import com.touchdown.perflowbackend.approval.query.dto.TemplateDetailResponseDTO;
import com.touchdown.perflowbackend.approval.query.dto.TemplateListResponseDTO;
import com.touchdown.perflowbackend.approval.query.repository.TemplateQueryRepository;
import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateQueryService {

    private final TemplateQueryRepository templateQueryRepository;

    @Transactional
    public Page<TemplateListResponseDTO> getTemplates(Pageable pageable) {

        return templateQueryRepository.findAllTemplates(pageable)
                .map(TemplateMapper::toTemplateResponseDTO);
    }

    public TemplateDetailResponseDTO getOne(Long templateId) {

        return templateQueryRepository.findOne(templateId)
                .map(TemplateMapper::toTemplateDetailResponseDTO)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_TEMPLATE));
    }
}
