package com.touchdown.perflowbackend.hr.query.controller;

import com.touchdown.perflowbackend.hr.query.dto.PositionResponseDTO;
import com.touchdown.perflowbackend.hr.query.service.PositionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class PositionQueryController {

    private final PositionQueryService positionQueryService;

    @GetMapping("/position")
    public ResponseEntity<List<PositionResponseDTO>> getPosition() {

        List<PositionResponseDTO> positionDTOList = positionQueryService.getAllPosition();

        return ResponseEntity.ok(positionDTOList);
    }
}
