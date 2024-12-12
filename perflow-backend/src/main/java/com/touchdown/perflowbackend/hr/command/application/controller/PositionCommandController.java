package com.touchdown.perflowbackend.hr.command.application.controller;

import com.touchdown.perflowbackend.common.exception.SuccessCode;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionCreateDTO;
import com.touchdown.perflowbackend.hr.command.application.dto.position.PositionUpdateDTO;
import com.touchdown.perflowbackend.hr.command.application.service.PositionCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PositionCommandController {

    private final PositionCommandService positionCommandService;

    @PostMapping("/hr/position")
    public ResponseEntity<SuccessCode> createPosition(
            @RequestBody PositionCreateDTO positionCreateDTO
    ) {

        positionCommandService.createPosition(positionCreateDTO);

        return ResponseEntity.ok(SuccessCode.POSITION_CREATE_SUCCESS);
    }

    @PutMapping("/hr/position")
    public ResponseEntity<SuccessCode> updatePosition(
            @RequestBody PositionUpdateDTO positionUpdateDTO
    ) {

        positionCommandService.updatePosition(positionUpdateDTO);

        return ResponseEntity.ok(SuccessCode.POSITION_UPDATE_SUCCESS);
    }

}
