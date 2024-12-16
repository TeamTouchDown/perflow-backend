package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.perfomance.command.infrastructure.repository.HrPerfoInquiryRepository;
import com.touchdown.perflowbackend.perfomance.query.dto.HrPerfoInquiryResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.HrPerfoInquiryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HrPerfoInquiryService {

    private final HrPerfoInquiryQueryRepository hrPerfoInquiryQueryRepository;
    // 인사 평가 의의제기 조회
    @Transactional(readOnly = true)
    public List<HrPerfoInquiryResponseDTO> getHrPerfoInquiry(Long deptId) {

        // 정보 불러오기
        List<HrPerfoInquiryResponseDTO> response = hrPerfoInquiryQueryRepository.findbydeptId(deptId, java.time.Year.now().getValue());

        return response;
    }
}
