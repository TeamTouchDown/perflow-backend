package com.touchdown.perflowbackend.menu.command.domain.aggregate;

import com.touchdown.perflowbackend.authority.domain.aggregate.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Menu parent;

    @Column(name = "name", nullable = false, length = 30)
    private String name;

    @Column(name = "url", nullable = false)
    private String url;

    @Setter
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;

}