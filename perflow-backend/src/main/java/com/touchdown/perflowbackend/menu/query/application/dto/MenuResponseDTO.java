package com.touchdown.perflowbackend.menu.query.application.dto;

import com.touchdown.perflowbackend.menu.command.domain.aggregate.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MenuResponseDTO {

    private Long id;

    private Long parentId;

    private String name;

    private String url;

    private Long authorityId;

    @Builder
    public MenuResponseDTO(Menu menu) {

        this.id = menu.getId();
        if(menu.getParent() != null) {
            this.parentId = menu.getParent().getId();
        }
        this.name = menu.getName();
        this.url = menu.getUrl();
        this.authorityId = menu.getAuthority().getAuthorityId();
    }
}
