package com.touchdown.perflowbackend.workAttitude.query.controller;

import com.touchdown.perflowbackend.workAttitude.query.dto.WorkAttitudeTravelResponseDTO;
import com.touchdown.perflowbackend.workAttitude.query.service.WorkAttitudeTravelQueryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "WorkAttribute-Controller", description = "출장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class WorkAttitudeTravelQueryController {

    private final WorkAttitudeTravelQueryService workAttitudeTravelQueryService;

    //출장 조회(사원)
    @GetMapping("/emp/travel/{travelId}")
    public ResponseEntity<WorkAttitudeTravelResponseDTO> getTravel(@PathVariable(name = "travelId") Long travelId) {
        WorkAttitudeTravelResponseDTO dto = workAttitudeTravelQueryService.getTravelById(travelId);
        return ResponseEntity.ok(dto);
    }


    @GetMapping ("/leader/travel")
    public ResponseEntity<List<WorkAttitudeTravelResponseDTO>>getAllTravelsForLeader(){
        List<WorkAttitudeTravelResponseDTO>dto = workAttitudeTravelQueryService.getAllTravelsForLeader();
        return ResponseEntity.ok(dto);
    }

}
