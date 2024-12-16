package com.touchdown.perflowbackend.menu.query.application.controller;

import com.touchdown.perflowbackend.menu.query.application.dto.MenuResponseDTO;
import com.touchdown.perflowbackend.menu.query.application.service.MenuQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/menu")
public class MenuQueryController {

    private final MenuQueryService menuQueryService;

    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getMenuList() {

        List<MenuResponseDTO> list = menuQueryService.getMenuList();

        return ResponseEntity.ok(list);
    }
}
