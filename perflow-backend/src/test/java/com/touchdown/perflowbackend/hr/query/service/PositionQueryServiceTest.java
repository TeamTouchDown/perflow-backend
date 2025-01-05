package com.touchdown.perflowbackend.hr.query.service;

import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.query.dto.PositionResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.PositionResponseListDTO;
import com.touchdown.perflowbackend.hr.query.repository.PositionQueryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PositionQueryServiceTest {

    @Mock
    private PositionQueryRepository positionQueryRepository;

    @InjectMocks
    private PositionQueryService positionQueryService;

    private Pageable pageable;
    private List<Position> mockPositionList;

    @BeforeEach
    void setUp() {
        // Pageable 설정 (page=0, size=5, 정렬)
        pageable = PageRequest.of(0, 5, Sort.by("positionId").ascending());

        // 테스트용 Position 리스트
        mockPositionList = new ArrayList<>();

        Position p1 = new Position();
        p1.setPositionId(1L);
        p1.setName("대표이사");
        p1.setPositionLevel(1);

        Position p2 = new Position();
        p2.setPositionId(2L);
        p2.setName("이사");
        p2.setPositionLevel(2);

        mockPositionList.add(p1);
        mockPositionList.add(p2);
    }

    @Test
    @DisplayName("getAllPosition - 정상적으로 페이징 조회 시, DTO 목록과 페이징 정보 반환")
    void testGetAllPosition_success() {
        // given
        // Page<Position> 생성 (content=mockPositionList, pageable, totalElements=2)
        Page<Position> mockPage = new PageImpl<>(mockPositionList, pageable, mockPositionList.size());

        // positionQueryRepository.findAll(pageable) 호출 시 mockPage 반환
        given(positionQueryRepository.findAll(any(Pageable.class)))
                .willReturn(mockPage);

        // when
        PositionResponseListDTO result = positionQueryService.getAllPosition(pageable);

        // then
        // repository 호출 확인
        then(positionQueryRepository).should(times(1)).findAll(pageable);

        // result 검증
        assertNotNull(result);
        // positions 배열 길이 = 2
        assertEquals(2, result.getPositions().size());

        // 첫 번째 DTO 확인 (매퍼 로직에 따라 필드가 어떻게 매핑되는지 검증)
        PositionResponseDTO firstDto = result.getPositions().get(0);
        assertEquals("대표이사", firstDto.getName());
        // positionLevel, id 등 필요 시 검증

        // 페이징 정보 확인
        assertEquals(1, result.getTotalPages());              // totalElements=2, size=5 → totalPages=1
        assertEquals(2, result.getTotalItems());              // totalElements=2
        assertEquals(1, result.getCurrentPage());             // page=0 → currentPage=1
        assertEquals(5, result.getPageSize());                // size=5
    }

    @Test
    @DisplayName("getAllPosition - 데이터가 없을 때, 빈 목록과 페이징 정보 반환")
    void testGetAllPosition_empty() {
        // given
        List<Position> emptyList = new ArrayList<>();
        Page<Position> emptyPage = new PageImpl<>(emptyList, pageable, 0);

        given(positionQueryRepository.findAll(any(Pageable.class)))
                .willReturn(emptyPage);

        // when
        PositionResponseListDTO result = positionQueryService.getAllPosition(pageable);

        // then
        assertNotNull(result);
        assertTrue(result.getPositions().isEmpty());  // 빈 목록
        assertEquals(1, result.getCurrentPage());     // page=0
        assertEquals(0, result.getTotalPages());      // totalElements=0 → totalPages=0
        assertEquals(0, result.getTotalItems());
    }
}