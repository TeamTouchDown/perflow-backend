package com.touchdown.perflowbackend.menu.query.application.mapper;

import com.touchdown.perflowbackend.menu.command.domain.aggregate.Menu;
import com.touchdown.perflowbackend.menu.query.application.dto.MenuResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class MenuMapper {

    public static List<MenuResponseDTO> toResponse(List<Menu> menuList) {

        List<MenuResponseDTO> menuResponseDTOList = new ArrayList<>();

        for (Menu menu : menuList) {
            menuResponseDTOList.add(
                    MenuResponseDTO.builder()
                            .menu(menu)
                            .build()
            );
        }

        return menuResponseDTOList;
    }
}
