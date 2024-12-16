package com.touchdown.perflowbackend.menu.query.application.service;

import com.touchdown.perflowbackend.menu.command.domain.aggregate.Menu;
import com.touchdown.perflowbackend.menu.query.application.dto.MenuResponseDTO;
import com.touchdown.perflowbackend.menu.query.application.mapper.MenuMapper;
import com.touchdown.perflowbackend.menu.query.application.repository.MenuQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuQueryService {

    private final MenuQueryRepository menuQueryRepository;

    public List<MenuResponseDTO> getMenuList() {

        List<Menu> menuList = menuQueryRepository.findAll();

        return MenuMapper.toResponse(menuList);
    }
}
