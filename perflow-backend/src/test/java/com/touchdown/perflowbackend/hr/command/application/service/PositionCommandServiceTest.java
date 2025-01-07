package com.touchdown.perflowbackend.hr.command.application.service;

import com.touchdown.perflowbackend.common.exception.CustomException;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionUpdateDTO;
import com.touchdown.perflowbackend.hr.command.domain.aggregate.Position;
import com.touchdown.perflowbackend.hr.command.domain.repository.PositionCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PositionCommandServiceTest {
    @Mock
    private PositionCommandRepository positionCommandRepository;

    @InjectMocks
    private PositionCommandService positionCommandService;

    private PositionCreateDTO createDTO;

    private Position existingPosition;
    private PositionUpdateDTO updateDTO;

    @BeforeEach
    void setUp() {
        // 테스트용 DTO
        createDTO = new PositionCreateDTO();
        createDTO.setName("부장");
        createDTO.setPositionLevel(3);

        // 기존 엔티티: positionId=100, name="사원", level=7
        existingPosition = new Position();
        existingPosition.setPositionId(100L);
        existingPosition.setName("사원");
        existingPosition.setPositionLevel(7);

        // 업데이트에 사용할 DTO
        updateDTO = new PositionUpdateDTO();
        updateDTO.setPositionId(100L);
        updateDTO.setName("대리");
        updateDTO.setPositionLevel(6);
    }

    @Test
    @DisplayName("createPosition - 정상적으로 직위가 생성되는 경우")
    void testCreatePosition_success() {
        // given
        // repository.save(...)가 호출되면, ID가 세팅된 Position을 반환한다고 가정
        Position savedPosition = new Position();
        savedPosition.setPositionId(100L);
        savedPosition.setName("부장");
        savedPosition.setPositionLevel(3);

        given(positionCommandRepository.save(any(Position.class)))
                .willReturn(savedPosition);

        // when
        positionCommandService.createPosition(createDTO);

        // then
        // save가 제대로 호출되었는지 검증
        then(positionCommandRepository).should(times(1)).save(any(Position.class));
    }

    @Test
    @DisplayName("updatePosition - 정상 업데이트 시 필드가 변경되고 save가 호출된다.")
    void testUpdatePosition_success() {
        // given
        given(positionCommandRepository.findById(updateDTO.getPositionId()))
                .willReturn(Optional.of(existingPosition));

        // when
        positionCommandService.updatePosition(updateDTO);

        // then
        // 엔티티 필드 변경 검증
        assertEquals("대리", existingPosition.getName());
        assertEquals(6, existingPosition.getPositionLevel());

        // save(...) 호출 검증
        then(positionCommandRepository).should(times(1)).save(existingPosition);
    }

    @Test
    @DisplayName("updatePosition - 해당 positionId가 존재하지 않을 경우 예외 발생")
    void testUpdatePosition_notFoundPosition() {
        // given
        given(positionCommandRepository.findById(updateDTO.getPositionId()))
                .willReturn(Optional.empty());

        // when & then
        CustomException ex = assertThrows(CustomException.class,
                () -> positionCommandService.updatePosition(updateDTO));

        // 예외 코드 검증 (프로젝트 구조에 맞게)
        // assertEquals(ErrorCode.NOT_FOUND_POSITION, ex.getErrorCode());

        // save(...)는 호출되지 않아야 함
        then(positionCommandRepository).should(never()).save(any(Position.class));
    }
}