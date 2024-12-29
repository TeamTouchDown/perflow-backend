package com.touchdown.perflowbackend.hr.query.controller;

import com.touchdown.perflowbackend.hr.query.dto.AppointResponseListDTO;
import com.touchdown.perflowbackend.hr.query.service.AppointQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hr/appoint")
public class AppointQueryController {

    private final AppointQueryService appointQueryService;

    @GetMapping
    public ResponseEntity<AppointResponseListDTO> getAppointList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Sort sort = Sort.by(Sort.Direction.DESC, "appointDate");

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        AppointResponseListDTO appointResponseListDTO = appointQueryService.getAppointList(pageable);

        return ResponseEntity.ok(appointResponseListDTO);
    }
}
