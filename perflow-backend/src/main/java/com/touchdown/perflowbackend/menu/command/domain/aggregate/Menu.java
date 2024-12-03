package com.touchdown.perflowbackend.menu.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "menu", schema = "perflow")
public class Menu {
    @Id
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

}