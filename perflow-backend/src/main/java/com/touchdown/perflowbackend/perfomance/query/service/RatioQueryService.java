package com.touchdown.perflowbackend.perfomance.query.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.common.exception.ErrorCode;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Department;
import com.touchdown.perflowbackend.hr.command.domain.repository.DepartmentCommandRepository;
import com.touchdown.perflowbackend.perfomance.command.domain.aggregate.Weight;
import com.touchdown.perflowbackend.perfomance.query.dto.RatioPerfoResponseDTO;
import com.touchdown.perflowbackend.perfomance.query.repository.WeightQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RatioQueryService {

    private final DepartmentCommandRepository departmentCommandRepository;
    private final WeightQueryService weightQueryService;

    // 인사 평가 비율 조회
    @Transactional(readOnly = true)
    public RatioPerfoResponseDTO getPerfoWeight(Long deptId){

        // 부서가 존재하는지 체크하기
        Department dep = findDepartmentByDeptId(deptId);

        // 인사 평가 비율 정보 불러오기
        RatioPerfoResponseDTO weight = weightQueryService.findWeightByDeptId(deptId);

        return weight;
    }

    // 부서번호로 부서 정보 찾기
    private Department findDepartmentByDeptId(Long deptId) {
        return departmentCommandRepository.findById(deptId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_DEPARTMENT));
    }
}
