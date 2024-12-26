package com.touchdown.perflowbackend.hr.query.controller;

import com.touchdown.perflowbackend.hr.query.dto.PositionResponseDTO;
import com.touchdown.perflowbackend.hr.query.dto.PositionResponseListDTO;
import com.touchdown.perflowbackend.hr.query.service.PositionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr")
public class PositionQueryController {

    private final PositionQueryService positionQueryService;

    @GetMapping("/position")
    public ResponseEntity<PositionResponseListDTO> getPosition(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {

        Pageable pageable = PageRequest.of(page - 1, size);

        PositionResponseListDTO positionDTOList = positionQueryService.getAllPosition(pageable);

        return ResponseEntity.ok(positionDTOList);
    }
}
